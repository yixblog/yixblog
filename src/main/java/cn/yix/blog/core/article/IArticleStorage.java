package cn.yix.blog.core.article;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-20
 * Time: 下午8:38
 */
public interface IArticleStorage {

    public JSONObject queryArticles(int page, int pageSize, Date timeStart, Date timeEnd, int userId, String tag, String[] keywords, String sortKey);

    public JSONObject saveArticle(int userId, String title, String content, boolean topFlag,String[] tags);

    public JSONObject getUserTags(int userId);

    public JSONObject editArticle(int userId, int articleId, String title, String content,String[] tags);

    public JSONObject doAdminEditArticle(int adminId, int articleId, String title, String content, boolean topFlag,String[] tags);

    public JSONObject deleteArticle(int userId, int articleId);

    public JSONObject doAdminDeleteArticle(int adminId, int articleId);

    public JSONObject queryArticle(int articleId);

    public JSONObject queryTags(int topNumber);

}
