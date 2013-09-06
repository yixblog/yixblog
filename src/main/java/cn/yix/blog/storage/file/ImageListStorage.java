package cn.yix.blog.storage.file;

import cn.yix.blog.core.file.IImageListStorage;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-9-6
 * Time: 下午4:52
 */
@Repository("imageListStorage")
public class ImageListStorage implements IImageListStorage {
    private static final String ROOT_FOLDER = "upload";
    private String webRoot;

    public ImageListStorage() {
        webRoot = System.getProperty("web.root");
    }

    @Override
    public List<String> listAllImages() {
        List<String> imageList = new ArrayList<>();
        String rootPath = webRoot + File.separator + ROOT_FOLDER;
        listFiles(rootPath, imageList);
        return imageList;
    }

    private void listFiles(String rootPath, List<String> imageList) {
        File file = new File(rootPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] childrenFiles = file.listFiles();
                if (childrenFiles != null) {
                    for (File child : childrenFiles) {
                        listFiles(child.getAbsolutePath(), imageList);
                    }
                }
            } else {
                if (checkImageFile(file)) {
                    String filePath = file.getAbsolutePath();
                    imageList.add(transferToRelativePath(filePath));
                }
            }
        }
    }

    private String transferToRelativePath(String filePath) {
        filePath = filePath.replace(webRoot, "");
        filePath = filePath.replace(File.separator, "/");
        if (filePath.charAt(0) == '/') {
            filePath = filePath.substring(1);
        }
        return filePath;
    }

    private boolean checkImageFile(File file) {
        final String[] imageTypes = {".jpg", ".jpeg", ".bmp", ".png", ".gif"};
        String fileType = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
        for (String type : imageTypes) {
            if (type.equals(fileType)) {
                return true;
            }
        }
        return false;
    }


}
