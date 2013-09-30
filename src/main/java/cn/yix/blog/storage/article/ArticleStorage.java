package cn.yix.blog.storage.article;

import cn.yix.blog.core.article.IArticleStorage;
import cn.yix.blog.dao.beans.AccountBean;
import cn.yix.blog.dao.beans.AdminBean;
import cn.yix.blog.dao.beans.ArticleBean;
import cn.yix.blog.dao.mappers.AccountMapper;
import cn.yix.blog.dao.mappers.AdminMapper;
import cn.yix.blog.dao.mappers.ArticleMapper;
import cn.yix.blog.storage.AbstractStorage;
import cn.yix.blog.storage.article.datafix.ArticleQueryJSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-20
 * Time: 下午8:50
 */
@Repository("articleStorage")
public class ArticleStorage extends AbstractStorage implements IArticleStorage {

    @Resource(name = "sessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sessionFactory) {
        super.setSqlSessionFactory(sessionFactory);
    }

    @Override
    public JSONObject queryArticles(int page, int pageSize, Date timeStart, Date timeEnd, int userId, String tag, String[] keywords, String sortKey) {
        JSONObject params = initQueryParams(timeStart, timeEnd, userId, tag, keywords);
        if (sortKey == null) {
            sortKey = "addtime";
        }
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        List<ArticleBean> articles = null;
        if ("addtime".equals(sortKey)) {
            articles = articleMapper.listNewArticles(params, getRowBounds(page, pageSize));
        } else if ("replycount".equals(sortKey)) {
            articles = articleMapper.listHotArticles(params, getRowBounds(page, pageSize));
        }
        JSONObject res = new JSONObject();
        res.put("articles", articles);
        res.put("success", true);
        int totalCount = articleMapper.countArticles(params);
        setPageInfo(res, totalCount, page, pageSize);
        return res;
    }

    private JSONObject initQueryParams(Date timeStart, Date timeEnd, int userId, String tag, String[] keywords) {
        JSONObject params = new ArticleQueryJSON();
        if (timeStart != null) {
            params.put("addtimeBegin", timeStart.getTime());
        }
        if (timeEnd != null) {
            params.put("addtimeEnd", timeEnd.getTime());
        }
        if (keywords != null) {
            String[] keywordsForQuery = buildQueryKeywords(keywords);
            params.put("keywords", keywordsForQuery);
        }
        if (userId > 0) {
            params.put("userid", userId);
        }
        if (tag != null) {
            params.put("tag", tag);
        }
        return params;
    }

    private String[] buildQueryKeywords(String[] keywords) {
        String[] keywordsForQuery = new String[keywords.length];
        int index = 0;
        for (String keyword : keywords) {
            if (keyword.length() > 0) {
                keywordsForQuery[index++] = getQueryKeyword(keyword);
            }
        }
        keywordsForQuery = subStringArray(keywordsForQuery, 0, index);
        return keywordsForQuery;
    }

    private String[] subStringArray(String[] stringArray, int startIndex, int length) {
        String[] res = new String[length];
        System.arraycopy(stringArray, startIndex, res, 0, length);
        return res;
    }

    private String getQueryKeyword(String keyword) {
        return "%" + keyword + "%";
    }

    @Override
    @Transactional
    public JSONObject saveArticle(int userId, String title, String content, boolean topFlag, String[] tags) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        JSONObject res = new JSONObject();
        AccountBean user = accountMapper.getAccountById(userId);
        if (user == null) {
            setResult(res, false, "用户不存在");
            return res;
        }
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        ArticleBean article = new ArticleBean();
        article.setAuthor(user);
        article.setTitle(title);
        article.setContent(content);
        article.setTopFlag(topFlag);
        article.setAddTime(System.currentTimeMillis());
        article.setTags(Arrays.asList(tags));
        articleMapper.saveArticle(article);
        articleMapper.saveTag(article);
        setResult(res, true, "保存成功");
        res.put("article", article);
        return res;
    }

    @Override
    public JSONObject getUserTags(int userId) {
        JSONObject res = new JSONObject();
        List<String> tags = getMapper(ArticleMapper.class).getUserTags(userId);
        res.put("tags", tags);
        logger.debug("get user tags:" + res.toJSONString());
        return res;
    }

    @Override
    @Transactional
    public JSONObject editArticle(int userId, int articleId, String title, String content, String[] tags) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean user = accountMapper.getAccountById(userId);
        JSONObject res = new JSONObject();
        if (user == null) {
            setResult(res, false, "不存在的用户");
            return res;
        }
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        ArticleBean article = articleMapper.getArticle(articleId);
        if (article == null) {
            setResult(res, false, "不存在的文章");
            return res;
        }
        if (!article.getAuthor().equals(user)) {
            setResult(res, false, "不能修改其他作者的文章");
            return res;
        }
        article.setTitle(title);
        article.setContent(content);
        article.setEditTime(System.currentTimeMillis());
        article.setTags(Arrays.asList(tags));
        articleMapper.update(article);
        //tags
        articleMapper.clearArticleTags(article);
        articleMapper.saveTag(article);
        setResult(res, true, "修改成功");
        return res;
    }

    @Override
    @Transactional
    public JSONObject doAdminEditArticle(int adminId, int articleId, String title, String content, boolean topFlag, String[] tags) {
        AdminMapper adminMapper = getMapper(AdminMapper.class);
        AdminBean admin = adminMapper.getAdminById(adminId);
        JSONObject res = new JSONObject();
        if (admin == null) {
            setResult(res, false, "管理员不存在");
            return res;
        }
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        ArticleBean article = articleMapper.getArticle(articleId);
        if (article == null) {
            setResult(res, false, "文章不存在");
            return res;
        }
        article.setTopFlag(topFlag);
        article.setTitle(title);
        article.setContent(content);
        articleMapper.update(article);
        articleMapper.clearArticleTags(article);
        article.setTags(Arrays.asList(tags));
        articleMapper.saveTag(article);
        setResult(res, true, "修改成功");
        return res;
    }

    @Override
    @Transactional
    public JSONObject deleteArticle(int userId, int articleId) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean user = accountMapper.getAccountById(userId);
        JSONObject res = new JSONObject();
        if (user == null) {
            setResult(res, false, "用户不存在");
            return res;
        }
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        ArticleBean article = articleMapper.getArticle(articleId);
        if (article != null) {
            if (!article.getAuthor().equals(user)) {
                setResult(res, false, "不能删除其他作者的文章");
                return res;
            }
            articleMapper.delete(article);
        }
        setResult(res, true, "删除成功");
        return res;
    }

    @Override
    @Transactional
    public JSONObject doAdminDeleteArticle(int adminId, int articleId) {
        AdminMapper adminMapper = getMapper(AdminMapper.class);
        AdminBean admin = adminMapper.getAdminById(adminId);
        JSONObject res = new JSONObject();
        if (admin == null) {
            setResult(res, false, "管理员不存在");
            return res;
        }
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        ArticleBean article = articleMapper.getArticle(articleId);
        if (article != null) {
            articleMapper.delete(article);
        }
        setResult(res, true, "删除成功");
        return res;
    }

    @Override
    public JSONObject queryArticle(int articleId) {
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        ArticleBean articleBean = articleMapper.getArticle(articleId);
        JSONObject res = new JSONObject();
        if (articleBean == null) {
            setResult(res, false, "不存在的文章");
            return res;
        }
        res.put("article", articleBean);
        setResult(res, true, "操作成功");
        return res;
    }

    @Override
    public JSONObject queryArticleForEdit(int articleId, int userId) {
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        JSONObject res = new JSONObject();
        ArticleBean article = articleMapper.getArticle(articleId);
        if (article == null) {
            setResult(res, false, "要修改的文章不存在");
            return res;
        }
        if (article.getAuthor().getId() != userId) {
            setResult(res, false, "您无权修改本文章");
            return res;
        }
        setResult(res, true, "查询成功");
        res.put("article", article);
        return res;
    }

    @Override
    public JSONObject queryTags(int topNumber) {
        ArticleMapper articleMapper = getMapper(ArticleMapper.class);
        JSONObject res = new JSONObject();
        setResult(res, true, "查询成功");
        res.put("tags", articleMapper.listTags(topNumber));
        return res;
    }

    @Override
    public JSONObject queryArticleAuthors(int topnumber) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        List<AccountBean> accounts = accountMapper.listTopArticleAuthors(getRowBounds(1, topnumber));
        JSONObject res = new JSONObject();
        setResult(res, true, "查询成功");
        res.put("users", accounts);
        return res;
    }
}
