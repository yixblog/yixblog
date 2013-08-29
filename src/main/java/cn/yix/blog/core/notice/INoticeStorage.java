package cn.yix.blog.core.notice;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-6-26
 * Time: 下午4:38
 */
public interface INoticeStorage {
    public JSONObject queryNotices(int page,int pageSize);

    public JSONObject queryNotice(int noticeId);

    public JSONObject saveNotice(String title,String content);

    public JSONObject updateNotice(int noticeId,String title,String content);

    public JSONObject deleteNotice(int noticeId);
}
