package cn.yix.blog.dao.beans;

import cn.yix.blog.utils.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-23
 * Time: 下午9:43
 */
public class AccountBean implements DatabaseEntity {
    private int id;
    private String uid;
    private String pwd;
    private String nick;
    private String avatar;
    private String qq;
    private String weibo;
    private String email;
    private String tempEmail;
    private int articleCount;
    private boolean banFlag = false;
    private long regTime;
    private long lastLogin;
    private String sex;

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

    @JSONField(serialize = false)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBanFlag() {
        return banFlag;
    }

    public void setBanFlag(boolean banFlag) {
        this.banFlag = banFlag;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public boolean equals(Object o) {
        return o instanceof AccountBean && ((AccountBean) o).getId() == this.getId();
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }
}
