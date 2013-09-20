package cn.yix.blog.dao.beans;

import cn.yix.blog.utils.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 上午12:38
 */
public class CommentBean implements DatabaseEntity {
    private int id;
    private String title;
    private String content;
    private AccountBean author;
    private ArticleBean article;
    private long addTime;
    private int floor;

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

    public AccountBean getAuthor() {
        return author;
    }

    public void setAuthor(AccountBean author) {
        this.author = author;
    }

    public ArticleBean getArticle() {
        return article;
    }

    public void setArticle(ArticleBean article) {
        this.article = article;
    }

    @JSONField(serialize = false)
    public long getAddTime() {
        return addTime;
    }

    @JSONField(name = "addtimestring")
    public String getAddTimeString() {
        return DateUtils.getDateString(addTime, DateUtils.DATE_TIME_FORMAT);
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
