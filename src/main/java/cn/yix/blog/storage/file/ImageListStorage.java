package cn.yix.blog.storage.file;

import cn.yix.blog.core.file.IImageListStorage;
import cn.yix.blog.dao.beans.ImageBean;
import cn.yix.blog.dao.mappers.ImageMapper;
import cn.yix.blog.storage.AbstractStorage;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
        List<ImageBean> images = mapper.listUserImages(userId, getRowBounds(page, pageSize));
        JSONObject res = new JSONObject();
        setResult(res, true, "查询成功");
        res.put("images", images);
        return res;
    }


}
