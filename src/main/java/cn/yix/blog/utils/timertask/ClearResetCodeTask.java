package cn.yix.blog.utils.timertask;

import cn.yix.blog.core.admin.IAdminAccountStorage;
import cn.yix.blog.core.user.IUserAccountStorage;
import cn.yix.blog.dao.beans.AccountBean;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-27
 * Time: 下午7:14
 */
@Component("resetCodeClearer")
public class ClearResetCodeTask {

    protected final Logger logger = Logger.getLogger(getClass());
    private final ConcurrentHashMap<String, ClearTaskBean> taskMap = new ConcurrentHashMap<>();
    @Resource(name = "adminAccountStorage")
    private IAdminAccountStorage adminAccountStorage;
    @Resource(name = "userAccountStorage")
    private IUserAccountStorage userAccountStorage;

    public synchronized void doClear() {
        long currentTime = System.currentTimeMillis();
        for (String key : taskMap.keySet()) {
            ClearTaskBean task = taskMap.get(key);
            if (task.getExecuteTime() <= currentTime) {
                executeTask(task);
                taskMap.remove(key);
            }
        }
    }

    private void executeTask(ClearTaskBean task) {
        switch (task.getJobType()) {
            case ClearTaskBean.TYPE_CLEAR_USER_RESETPWD:
            case ClearTaskBean.TYPE_CLEAR_USER_RESETEMAIL:
                clearUserResetCode(task);
                break;
            case ClearTaskBean.TPPE_CLEAR_ADMIN_RESETPWD:
            case ClearTaskBean.TYPE_CLEAR_ADMIN_RESETEMAIL:
                clearAdminResetCode(task);
                break;
            case ClearTaskBean.TYPE_CLEAR_USER_CONFIRMEMAIL:
                clearUserTempEmail(task);
                break;
            case ClearTaskBean.TYPE_CLEAR_ADMIN_CONFIRMEMAIL:
                clearAdminTempEmail(task);
                break;
            case ClearTaskBean.TYPE_CLEAR_USER_BAN:
                clearUserBanFlag(task);
                break;
        }
    }

    private void clearUserBanFlag(ClearTaskBean task) {
        userAccountStorage.doUnbanUser(task.getId());
        logger.info("unban user:" + task.getAccountBean().getUid());
    }

    private void clearAdminTempEmail(ClearTaskBean task) {
        String tempEmail = task.getAdmin().getTempEmail();
        String uid = task.getAdmin().getUid();
        adminAccountStorage.doClearTempEmail(task.getId(), tempEmail);
        logger.info("cancel admin email change:" + uid + "&" + tempEmail);
    }

    private void clearUserTempEmail(ClearTaskBean task) {
        AccountBean user = task.getAccountBean();
        userAccountStorage.doClearTempEmail(user.getId(), user.getTempEmail());
        logger.info("cancel user email change:" + user.getUid() + "&" + user.getTempEmail());
    }

    private void clearAdminResetCode(ClearTaskBean task) {
        taskMap.remove(task.getResetCode().generateResetCode());
        logger.info("cancel admin password force change:" + task.getAdmin().getUid());
    }

    private void clearUserResetCode(ClearTaskBean task) {
        taskMap.remove(task.getResetCode().generateResetCode());
        logger.info("cancel user password force change:" + task.getAccountBean().getUid());
    }

    public synchronized void addTask(ClearTaskBean task) {
        checkSameTypeTask(task);
        taskMap.put(task.getResetCode().generateResetCode(), task);
    }

    private void checkSameTypeTask(ClearTaskBean task) {
        for (String key : taskMap.keySet()) {
            ClearTaskBean cTask = taskMap.get(key);
            if (cTask.getJobType() == task.getJobType() && isSameUserTask(cTask, task)) {
                taskMap.remove(key);
            }
        }
    }

    private boolean isSameUserTask(ClearTaskBean cTask, ClearTaskBean task) {
        return cTask.getId() == task.getId();
    }

    public ClearTaskBean queryTask(String resetCode) {
        return taskMap.get(resetCode);
    }

    public void clearTask(String resetCode) {
        taskMap.remove(resetCode);
    }
}
