package cn.yix.blog.storage.file;

import cn.yix.blog.core.file.IImageListStorage;
import cn.yix.blog.dao.beans.ImageBean;
import cn.yix.blog.dao.mappers.ImageMapper;
import cn.yix.blog.storage.AbstractStorage;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
public class ImageListStorage extends AbstractStorage implements IImageListStorage {

    private String webRoot;
    private Logger logger = Logger.getLogger(getClass());

    public ImageListStorage() {
        webRoot = System.getProperty("web.root");
    }

    @Resource(name = "sessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public List<String> listAllImages(int userid) {
        ImageMapper mapper = getMapper(ImageMapper.class);
        List<ImageBean> images = mapper.listUserImages(userid, new RowBounds());
        List<String> urls = new ArrayList<>();
        for (ImageBean image : images) {
            urls.add(image.getUrl());
        }
        return urls;
    }

    @Override
    public JSONObject listUserImages(int page, int pageSize, int userId) {
        ImageMapper mapper = getMapper(ImageMapper.class);
        int imageCount = mapper.getUserImageCount(userId);
        List<ImageBean> images = mapper.listUserImages(userId, getRowBounds(page, pageSize));
        JSONObject res = new JSONObject();
        setResult(res, true, "查询成功");
        setPageInfo(res, imageCount, page, pageSize);
        res.put("images", images);
        return res;
    }

    @Override
    public JSONObject deleteUserImage(int imageId, int userId) {
        ImageMapper mapper = getMapper(ImageMapper.class);
        ImageBean img = mapper.findOneImage(imageId);
        JSONObject res = new JSONObject();
        if (img.getUser().getId() != userId) {
            setResult(res, false, "您无权删除别人的图片");
            return res;
        }
        String imageRealPath = webRoot + "/" + img.getUrl();
        logger.debug("image real path to delete:" + imageRealPath);
        if (!FileUtils.deleteQuietly(new File(imageRealPath))) {
            setResult(res, false, "删除失败");
            return res;
        }
        mapper.deleteImage(imageId);
        setResult(res, true, "删除成功");
        return res;
    }


}
