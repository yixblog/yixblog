package cn.yix.blog.dao;

import cn.yix.blog.dao.beans.ArticleBean;
import cn.yix.blog.dao.mappers.ArticleMapper;
import cn.yix.blog.storage.AbstractStorage;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 下午12:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:web/WEB-INF/applicationContext.xml", "file:web/WEB-INF/dispatcher-servlet.xml"})
public class DAOTest extends AbstractStorage {
    @Resource(name = "sessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sessionFactory) {
        super.setSqlSessionFactory(sessionFactory);
    }

    @Test
    public void listArticles() {
        ArticleMapper mapper = getMapper(ArticleMapper.class);
        ArticleBean article = mapper.getArticle(2);
        JSONObject res = new JSONObject();
        res.put("article",article);
        System.out.println(res.toJSONString());
    }

}
