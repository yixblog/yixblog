package cn.yix.blog.dao.mappers;

import cn.yix.blog.dao.beans.ArticleBean;
import cn.yix.blog.dao.beans.TagCountBean;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 上午12:40
 */
public interface ArticleMapper {
    /**
     * list articles
     *
     * @param params   param map,available keys:title,addtimeBegin,addtimeEnd
     * @param pageArgs page args
     * @return list of ArticlesBean
     */
    public List<ArticleBean> listNewArticles(JSONObject params, RowBounds pageArgs);

    public List<ArticleBean> listHotArticles(JSONObject params, RowBounds pageArgs);

    public List<String> getArticleTags(int articleId);

    public List<String> getUserTags(int userId);

    public int countArticles(JSONObject params);

    public List<TagCountBean> listTags(int topNumber);

    public ArticleBean getArticle(int id);

    public List<ArticleBean> listArticlesByTag(String tag, RowBounds pageArgs);

    public int countArticlesByTag(String tag);

    public List<ArticleBean> listArticlesByAccount(int accountId, RowBounds pageArgs);

    public int countArticlesByAccount(int accountId);

    public void saveArticle(ArticleBean article);

    public void saveTag(ArticleBean article);

    public void update(ArticleBean article);

    public void delete(ArticleBean article);

    public void clearArticleTags(ArticleBean article);
}
