package cn.yix.blog.dao.beans;

import cn.yix.blog.utils.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-18
 * Time: 下午9:32
 */
public class NoticeBean implements DatabaseEntity{
    private int id;
    private String title;
    private String content;
    private long addTime;

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

    @JSONField(serialize = false)
    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @JSONField(name = "addtimestring")
    public String getAddTimeString(){
        return DateUtils.getDateString(addTime,DateUtils.DATE_TIME_FORMAT);
    }
}
