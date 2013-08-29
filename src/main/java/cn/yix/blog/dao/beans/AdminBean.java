package cn.yix.blog.dao.beans;

import cn.yix.blog.utils.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-11
 * Time: 上午9:15
 */
public class AdminBean implements DatabaseEntity{
    private int id;
    private String uid;
    private String pwd;
    private long regTime;
    private long lastLogin;
    private String email;
    private String tempEmail;
    private boolean systemConfig;
    private boolean userManage;
    private boolean adminManage;
    private boolean articleManage;
    private boolean commentManage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;

    }

    @JSONField(name = "systemconfig")
    public boolean isSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(boolean systemConfig) {
        this.systemConfig = systemConfig;
    }

    @JSONField(name = "usermanage")
    public boolean isUserManage() {
        return userManage;
    }

    public void setUserManage(boolean userManage) {
        this.userManage = userManage;
    }

    @JSONField(name = "adminmanage")
    public boolean isAdminManage() {
        return adminManage;
    }

    public void setAdminManage(boolean adminManage) {
        this.adminManage = adminManage;
    }

    @JSONField(name = "articlemanage")
    public boolean isArticleManage() {
        return articleManage;
    }

    public void setArticleManage(boolean articleManage) {
        this.articleManage = articleManage;
    }

    @JSONField(name = "commentmanage")
    public boolean isCommentManage() {
        return commentManage;
    }

    public void setCommentManage(boolean commentManage) {
        this.commentManage = commentManage;
    }

    @JSONField(serialize = false)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @JSONField(name = "regtime")
    public long getRegTime() {
        return regTime;
    }

    public void setRegTime(long regTime) {
        this.regTime = regTime;
    }

    @JSONField(name = "lastlogin")
    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JSONField(name = "regtimestring")
    public String getRegTimeString() {
        return DateUtils.getDateString(regTime, DateUtils.DATE_TIME_FORMAT);
    }

    @JSONField(name = "lastloginstring")
    public String getLastLoginString() {
        return lastLogin > 0 ? DateUtils.getDateString(lastLogin, DateUtils.DATE_TIME_FORMAT) : "";
    }

    @JSONField(serialize = false)
    public String getTempEmail() {
        return tempEmail;
    }

    public void setTempEmail(String tempEmail) {
        this.tempEmail = tempEmail;
    }
}
