package cn.yix.blog.storage.file;

import cn.yix.blog.core.file.IImageListStorage;
import cn.yix.blog.dao.beans.ImageBean;
import cn.yix.blog.dao.mappers.ImageMapper;
import cn.yix.blog.storage.AbstractStorage;
import org.apache.ibatis.session.SqlSessionFactory;
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

    @Resource(name = "sessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
    @Override
    public List<String> listAllImages(int userid) {
        ImageMapper mapper = getMapper(ImageMapper.class);
        List<ImageBean> images = mapper.listUserImages(userid);
        List<String> urls = new ArrayList<>();
        for (ImageBean image : images) {
            urls.add(image.getUrl());
        }
        return urls;
    }


}
