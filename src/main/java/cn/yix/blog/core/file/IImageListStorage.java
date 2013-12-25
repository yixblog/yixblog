package cn.yix.blog.core.file;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-9-6
 * Time: 下午4:51
 * 图片管理器
 */
public interface IImageListStorage {
    public List<String> listAllImages(int userid);

    public JSONObject listUserImages(int page, int pageSize, int id);

    public JSONObject deleteUserImage(int imageId, int userId);
}
