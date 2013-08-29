package cn.yix.blog.core.comment;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-6-26
 * Time: 下午1:18
 */
public interface ICommentStorage {
    public JSONObject queryArticleComments(int articleId,int page,int pageSize);

    public JSONObject queryUserComments(int userId,int page,int pageSize);

    public JSONObject saveComment(int userId,int articleId,String title,String content);

    public JSONObject deleteComment(int commentId);

    public JSONObject queryCommentsToUser(int id, int page, int pageSize);
}
