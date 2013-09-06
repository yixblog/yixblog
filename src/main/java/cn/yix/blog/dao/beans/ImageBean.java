package cn.yix.blog.dao.beans;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-6
 * Time: 下午10:19
 */
public class ImageBean {
    private int id;
    private String url;
    private AccountBean user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AccountBean getUser() {
        return user;
    }

    public void setUser(AccountBean user) {
        this.user = user;
    }
}
