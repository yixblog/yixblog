package cn.yix.blog.dao.beans;

import cn.yix.blog.utils.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 上午12:35
 */
public class ArticleBean implements DatabaseEntity {
    private int id;
    private String title;
    private String content;
    private long addTime;
    private long editTime;
    private int replyCount;
    private AccountBean author;
    private boolean topFlag = false;
    private List<String> tags;

    @JSONField(name = "replycount")
    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @JSONField(name = "addtimestring")
    public String getAddTimeString() {
        return DateUtils.getDateString(addTime, "yyyy-MM-dd HH:mm:ss");
    }

    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }

    @JSONField(name = "edittimestring")
    public String getEditTimeString() {
        return DateUtils.getDateString(editTime, "yyyy-MM-dd HH:mm:ss");
    }

    public AccountBean getAuthor() {
        return author;
    }

    public void setAuthor(AccountBean author) {
        this.author = author;
    }

    public boolean isTopFlag() {
        return topFlag;
    }

    public void setTopFlag(boolean topFlag) {
        this.topFlag = topFlag;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
