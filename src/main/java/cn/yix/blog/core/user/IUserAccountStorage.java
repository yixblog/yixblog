package cn.yix.blog.core.user;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-4
 * Time: 下午3:27
 */
public interface IUserAccountStorage {
    /**
     * query users
     *
     * @param param    key:uid,nick,qq,weibo,email,regtimeBegin,regtimeEnd
     * @param page     page number,start with 1
     * @param pageSize page size
     * @return JSONObject contains list
     */
    public JSONObject queryUsers(JSONObject param, int page, int pageSize);

    public JSONObject queryUser(int id);

    public JSONObject doUserLogin(String uid, String pwd);

    public JSONObject queryForgetPasswordRequest(String uid, String email);

    public JSONObject queryResetEvent(String resetCode);

    public JSONObject doForceChangePassword(String resetCode, String newPwd);

    public JSONObject doConfirmEmail(String resetCode);

    public JSONObject requestConfirmEmail(int id);

    public JSONObject doChangePwd(int id, String oldPwd, String newPwd);

    public JSONObject doChangeEmail(int id, String email);

    public JSONObject doClearTempEmail(int id, String email);

    public JSONObject doRegisterUser(String uid, String pwd, String nick, String email, String sex);

    public JSONObject doBindQQ(int id, String qq);

    public JSONObject doBindWeibo(int id, String weibo);

    public JSONObject doQQLogin(String qq);

    public JSONObject doWeiboLogin(String weibo);

    public JSONObject editUser(int id,String pwd,String nick,String email,String sex,String avatar);

    public JSONObject doBanUser(int id,int days);

    public void doUnbanUser(int id);

    public void doUnbindQQ(int id);

    public void doUnbindWeibo(int id);
}
