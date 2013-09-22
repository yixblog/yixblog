package cn.yix.blog.core.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-4
 * Time: 上午7:40
 */
public interface IFileSavingStorage {
    /**
     * 保存上传文件
     *
     * @param file         文件名
     * @param pictitle     文件标题
     * @param allowedTypes 允许的后缀名
     * @param maxSize      最大大小（KB）
     * @param userId       登陆用户id
     * @return 保存信息
     */
    public SavingResultInfo doSaveFile(MultipartFile file, String pictitle, String[] allowedTypes, int maxSize, int userId);

    /**
     * 保存二进制流
     *
     * @param content      文件字节流转化的字符串
     * @param allowedTypes 允许的后缀名
     * @param maxSize      最大大小（KB）
     * @param userId       登陆用户ID
     * @return 保存信息
     */
    public SavingResultInfo doSaveBase64(String content, String[] allowedTypes, int maxSize, int userId);

    /**
     * 远程获取图片并保存到本地
     *
     * @param upfile   文件URL
     * @param fileType 允许的文件类型
     * @param maxSize  最大大小（KB）
     * @param userId   登陆用户ID
     * @return 保存信息
     */
    public SavingResultInfo doSaveRemoteImage(String upfile, String[] fileType, int maxSize, int userId);
}
