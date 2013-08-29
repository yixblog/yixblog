package cn.yix.blog.core.admin;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 下午3:13
 */
public interface IAdminAccountStorage {
    public JSONObject doLogin(String uid, String pwd);

    public JSONObject saveAdminAccount(String uid, String pwd, String email);

    public JSONObject queryForgetPasswordRequest(String uid, String email);

    public JSONObject doForceResetPassword(String resetCode, String pwd);

    public JSONObject doChangePassword(int id,String oldPwd, String newPwd);

    public JSONObject queryResetEmailRequest(int id);

    public JSONObject doConfirmEmail(String resetCode);

    public JSONObject queryAdminById(int id);

    public JSONObject queryAdminList(String uid,String email,int page,int pageSize);

    public void doClearTempEmail(int id,String email);

    public JSONObject doResetEmail(String resetCode, String email);

    public JSONObject queryResetCode(String resetCode);

    public JSONObject deleteAdmin(int id);

    public JSONObject doEditAdmin(int id, String pwd, String email);
}
