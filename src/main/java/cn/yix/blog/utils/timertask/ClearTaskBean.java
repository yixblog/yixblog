package cn.yix.blog.utils.timertask;

import cn.yix.blog.dao.beans.AccountBean;
import cn.yix.blog.dao.beans.AdminBean;
import cn.yix.blog.utils.bean.ResetCode;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-27
 * Time: 下午7:28
 */
public class ClearTaskBean {
    public static final int TYPE_CLEAR_USER_RESETPWD=1;
    public static final int TPPE_CLEAR_ADMIN_RESETPWD=2;
    public static final int TYPE_CLEAR_USER_RESETEMAIL=3;
    public static final int TYPE_CLEAR_ADMIN_RESETEMAIL=4;
    public static final int TYPE_CLEAR_USER_CONFIRMEMAIL=5;
    public static final int TYPE_CLEAR_ADMIN_CONFIRMEMAIL=6;
    public static final int TYPE_CLEAR_USER_BAN = 7;
    private ResetCode resetCode;
    private long executeTime;
    private int id;
    private int jobType;
    private AdminBean admin;
    private AccountBean accountBean;

    public ClearTaskBean(ResetCode resetCode, long executeTime, int id, int jobType) {
        this.resetCode = resetCode;
        this.executeTime = executeTime;
        this.id = id;
        this.jobType = jobType;
    }

    private ClearTaskBean(){}

    public static ClearTaskBean generateUnbanUserTask(AccountBean user,long executeTime){
        ClearTaskBean task = new ClearTaskBean();
        task.setId(user.getId());
        task.setAccountBean(user);
        task.setExecuteTime(executeTime);
        task.setJobType(TYPE_CLEAR_USER_BAN);
        return task;
    }

    public ResetCode getResetCode() {
        return resetCode;
    }

    public void setResetCode(ResetCode resetCode) {
        this.resetCode = resetCode;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public AdminBean getAdmin() {
        return admin;
    }

    public void setAdmin(AdminBean admin) {
        this.admin = admin;
    }

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }
}
