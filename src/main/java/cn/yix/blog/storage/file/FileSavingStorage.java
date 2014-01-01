package cn.yix.blog.storage.file;

import cn.yix.blog.core.file.IFileSavingStorage;
import cn.yix.blog.core.file.SavingResultInfo;
import cn.yix.blog.dao.beans.AccountBean;
import cn.yix.blog.dao.beans.ImageBean;
import cn.yix.blog.dao.mappers.AccountMapper;
import cn.yix.blog.dao.mappers.ImageMapper;
import cn.yix.blog.storage.AbstractStorage;
import cn.yix.blog.utils.UEditorConfig;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-4
 * Time: 上午7:41
 * 文件保存类
 */
@Repository("fileSavingStorage")
public class FileSavingStorage extends AbstractStorage implements IFileSavingStorage {
    private static final String SAVE_DIR = "upload";
    private Map<String, String> errorCode = new HashMap<>();
    private String webRoot;
    private Logger logger = Logger.getLogger(getClass());

    public FileSavingStorage() {
        initErrorCode();
        webRoot = System.getProperty("web.root");
    }

    @Resource(name = "sessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    private void initErrorCode() {
        errorCode.put("SUCCESS", "SUCCESS"); //默认成功
        errorCode.put("NOFILE", "未包含文件上传域");
        errorCode.put("TYPE", "不允许的文件格式");
        errorCode.put("SIZE", "文件大小超出限制");
        errorCode.put("ENTYPE", "请求类型ENTYPE错误");
        errorCode.put("REQUEST", "上传请求异常");
        errorCode.put("IO", "IO异常");
        errorCode.put("DIR", "目录创建失败");
        errorCode.put("UNKNOWN", "未知错误");
        errorCode.put("URL", "请求地址不存在！");
        errorCode.put("HTTPHEAD", "请求地址头不正确");
    }

    @Override
    @Transactional
    public SavingResultInfo doSaveFile(MultipartFile file, String pictitle, String[] allowedTypes, int maxSize, int userId) {
        SavingResultInfo result = new SavingResultInfo();
        result.setOriginalName(file.getOriginalFilename());
        result.setSize(file.getSize());
        result.setTitle(pictitle);
        if (!checkLegal(result, allowedTypes, maxSize)) {
            return result;
        }
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            result.setState(errorCode.get("UNKNOWN"));
            return result;
        }
        return saveFileBytes(result, fileBytes, userId);
    }

    @Override
    @Transactional
    public SavingResultInfo doSaveBase64(String fileContent, String[] allowedTypes, int maxSize, int userId) {
        SavingResultInfo result = new SavingResultInfo();
        result.setType(".png");
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] fileBytes = decoder.decodeBuffer(fileContent);
            saveFileBytes(result, fileBytes, userId);
        } catch (IOException e) {
            result.setState(errorCode.get("IO"));
        }
        return result;
    }

    @Override
    @Transactional
    public SavingResultInfo doSaveRemoteImage(String upfile, String[] allowedTypes, int maxSize, int userId) {
        String[] sourceUrls = upfile.split(UEditorConfig.UE_SEPERATOR);
        List<String> outSrcs = new ArrayList<>();
        SavingResultInfo result = new SavingResultInfo();
        for (String sourceUrl : sourceUrls) {
            result.setType(sourceUrl.substring(sourceUrl.lastIndexOf(".")));
            if (!checkLegal(result, allowedTypes, maxSize)) {
                return result;
            }
            try {
                byte[] imageBytes = getUrlImageBytes(sourceUrl, result);
                if (imageBytes == null) {
                    if (result.getState() == null) {
                        result.setState(errorCode.get("UNKNOWN"));
                    }
                    return result;
                }
                saveFileBytes(result, imageBytes, userId);
                outSrcs.add(result.getUrl());
            } catch (IOException e) {
                result.setState(errorCode.get("URL"));
                return result;
            }
        }
        result.setUrl(UEditorConfig.combineUrls(outSrcs));
        return result;
    }

    private byte[] getUrlImageBytes(String sourceUrl, SavingResultInfo result) throws IOException {
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection conn = (HttpURLConnection) new URL(sourceUrl).openConnection();
        if (!conn.getContentType().contains("image")) {
            result.setState(errorCode.get("HTTPHEAD"));
            return null;
        }
        if (conn.getResponseCode() != 200) {
            result.setState(errorCode.get("URL"));
            return null;
        }
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        InputStream ins = conn.getInputStream();
        int b;
        while ((b = ins.read()) != -1) {
            ous.write(b);
        }
        ins.close();
        return ous.toByteArray();
    }

    private SavingResultInfo saveFileBytes(SavingResultInfo result, byte[] fileBytes, int userId) {
        String savingFolder = generateSavingFolder();
        String fileName = generateFileName(result.getType());
        if (!createFolder(savingFolder)) {
            result.setState(errorCode.get("DIR"));
            return result;
        }
        String targetFile = webRoot + File.separator + savingFolder + File.separator + fileName;
        logger.debug("saving file to:" + targetFile);
        try {
            FileUtils.writeByteArrayToFile(new File(targetFile), fileBytes);
        } catch (IOException e) {
            result.setState(errorCode.get("IO"));
            return result;
        }
        String url = generateUrl(savingFolder, fileName);
        logger.debug("request url:" + url);
        if (isImageFile(result.getType())) {
            saveFileToDatabase(url, userId);
        }
        result.setUrl(url);
        result.setState(errorCode.get("SUCCESS"));
        return result;
    }

    private boolean isImageFile(String type) {
        final String[] imageTypes = {".jpg", ".jpeg", ".bmp", ".png", ".gif"};
        for (String imageType : imageTypes) {
            if (imageType.equals(type)) {
                return true;
            }
        }
        return false;
    }

    private void saveFileToDatabase(String url, int userId) {
        ImageMapper imageMapper = getMapper(ImageMapper.class);
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean user = accountMapper.getAccountById(userId);
        if (user == null) {
            return;
        }
        ImageBean image = new ImageBean();
        image.setUrl(url);
        image.setUser(user);
        imageMapper.saveImage(image);
    }

    private String generateUrl(String savingFolder, String fileName) {
        return savingFolder.replace("\\", "/") + "/" + fileName;
    }

    private boolean createFolder(String savingFolder) {
        String path = webRoot + File.separator + savingFolder;
        logger.debug("target saving folder:" + path);
        File file = new File(path);
        return file.exists() || file.mkdirs();
    }

    private boolean checkLegal(SavingResultInfo result, String[] allowedTypes, int maxSize) {
        return checkLegalSize(result, maxSize) && checkLegalType(result, allowedTypes);
    }

    private boolean checkLegalSize(SavingResultInfo result, int maxSize) {
        if (result.getSize() > maxSize * 1024) {
            result.setState(errorCode.get("SIZE"));
            return false;
        }
        return true;
    }

    private boolean checkLegalType(SavingResultInfo result, String[] allowedTypes) {
        String type = result.getType();
        for (String allowedType : allowedTypes) {
            if (allowedType.equals(type)) {
                return true;
            }
        }
        result.setState(errorCode.get("TYPE"));
        return false;
    }

    private String generateFileName(String fileType) {
        Random random = new Random();
        return "" + random.nextInt(10000)
                + System.currentTimeMillis() + fileType;
    }

    private String generateSavingFolder() {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return SAVE_DIR + File.separator + format.format(new Date());
    }
}
