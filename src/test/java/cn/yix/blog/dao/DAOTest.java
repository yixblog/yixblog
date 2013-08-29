package cn.yix.blog.dao;

import cn.yix.blog.dao.beans.CharacterBean;
import cn.yix.blog.dao.mappers.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 下午12:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:WEB-INF/appCxt.xml")
public class DAOTest extends SqlSessionDaoSupport{
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Test
    public void testConfiguration(){
        SqlSession sqlSession = getSqlSession();
        AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);
        AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
        ArticleMapper articleMapper = sqlSession.getMapper(ArticleMapper.class);
        CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
        CharacterMapper characterMapper = sqlSession.getMapper(CharacterMapper.class);
        CharacterBean superAdmin = new CharacterBean();
        superAdmin.setAdminManage(true);
        superAdmin.setArticleManage(true);
        superAdmin.setCommentManage(true);
        superAdmin.setUserManage(true);
        superAdmin.setSystemConfig(true);
        superAdmin.setName("超级管理员");
        characterMapper.save(superAdmin);
    }
}
