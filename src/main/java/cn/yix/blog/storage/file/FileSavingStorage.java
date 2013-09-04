package cn.yix.blog.storage.file;

import cn.yix.blog.core.file.IFileSavingStorage;
import cn.yix.blog.core.file.SavingResultInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-4
 * Time: 上午7:41
 * 文件保存类
 */
@Repository("fileSavingStorage")
public class FileSavingStorage implements IFileSavingStorage {
    private Map<String, String> errorCode = new HashMap<>();
    private String webRoot;
    private static final String SAVE_DIR = "upload";

    private Logger logger = Logger.getLogger(getClass());

    public FileSavingStorage() {
        initErrorCode();
        webRoot = System.getProperty("web.root");
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
    }

    @Override
    public SavingResultInfo saveFile(MultipartFile file, String[] allowedTypes, int maxSize) {
        SavingResultInfo result = new SavingResultInfo();
        result.setOriginalName(file.getOriginalFilename());
        result.setSize(file.getSize());
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
        return saveFileBytes(result, fileBytes);
    }

    @Override
    public SavingResultInfo saveBase64(byte[] fileBytes, String[] allowedTypes, int maxSize) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private SavingResultInfo saveFileBytes(SavingResultInfo result, byte[] fileBytes) {
        String savingFolder = generateSavingFolder();
        String fileName = generateFileName(result.getType());
        if (!createFolder(savingFolder)) {
            result.setState(errorCode.get("DIR"));
            return result;
        }
        String targetFile = webRoot + File.separator + savingFolder + File.separator + fileName;
        logger.debug("saving file to:" + targetFile);
        try {
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile));
            output.write(fileBytes);
            output.flush();
            output.close();
        } catch (IOException e) {
            result.setState(errorCode.get("IO"));
            return result;
        }
        String url = savingFolder.replace("\\", "/") + "/" + fileName;
        logger.debug("request url:" + url);
        result.setUrl(url);
        result.setState(errorCode.get("SUCCESS"));
        return result;
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
        if (result.getSize() > maxSize) {
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
