package cn.yix.blog.core.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-4
 * Time: 上午7:40
 */
public interface IFileSavingStorage {
    public SavingResultInfo saveFile(MultipartFile file,String[] allowedTypes,int maxSize);

    public SavingResultInfo saveBase64(byte[] fileBytes,String[] allowedTypes,int maxSize);
}
