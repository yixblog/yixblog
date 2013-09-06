package cn.yix.blog.core.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-4
 * Time: 上午7:40
 */
public interface IFileSavingStorage {
    public SavingResultInfo doSaveFile(MultipartFile file, String pictitle, String[] allowedTypes, int maxSize, int userId);

    public SavingResultInfo doSaveBase64(String content, String[] allowedTypes, int maxSize, int userId);

    public SavingResultInfo doSaveRemoteImage(String upfile, String[] fileType, int maxSize, int userId);
}
