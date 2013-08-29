package cn.yix.blog.storage.user;

import cn.yix.blog.core.user.IUserAccountStorage;
import cn.yix.blog.dao.beans.AccountBean;
import cn.yix.blog.dao.mappers.AccountMapper;
import cn.yix.blog.storage.AbstractStorage;
import cn.yix.blog.utils.ResetCodeFactory;
import cn.yix.blog.utils.StringUtil;
import cn.yix.blog.utils.VelocityMailTemplateSender;
import cn.yix.blog.utils.bean.ResetCode;
import cn.yix.blog.utils.timertask.ClearResetCodeTask;
import cn.yix.blog.utils.timertask.ClearTaskBean;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-12
 * Time: 上午9:56
 */
@Repository("userAccountStorage")
public class UserAccountStorage extends AbstractStorage implements IUserAccountStorage {

    @Resource(name = "mailTemplate")
    private VelocityMailTemplateSender mailTemplate;
    @Resource(name = "resetCodeClearer")
    private ClearResetCodeTask resetCodeTask;

    @Resource(name = "sessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public JSONObject queryUsers(JSONObject param, int page, int pageSize) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        JSONObject res = new JSONObject();
        Map<String, Object> params = buildQueryUserParam(param);
        int totalCount = accountMapper.countListAccounts(params);
        List<AccountBean> accounts = accountMapper.listAccounts(params, getRowBounds(page,pageSize));
        setResult(res, true, "执行成功");
        setPageInfo(res, totalCount, page, pageSize);
        res.put("accounts", accounts);
        return res;
    }

    private Map<String, Object> buildQueryUserParam(JSONObject param) {
        Map<String, Object> params = new HashMap<>();
        if (param.containsKey("uid")) {
            params.put("uid", "%" + param.getString("uid").toLowerCase() + "%");
        }
        if (param.containsKey("nick")) {
            params.put("nick", "%" + param.getString("nick") + "%");
        }
        if (param.containsKey("qq")) {
            params.put("qq", param.getString("qq"));
        }
        if (param.containsKey("weibo")) {
            params.put("weibo", param.getString("weibo").toLowerCase());
        }
        if (param.containsKey("email")) {
            params.put("email", param.getString("email").toLowerCase());
        }
        if (param.containsKey("regtimebegin")) {
            params.put("regtimeBegin", param.getLong("regtimebegin"));
        }
        if (param.containsKey("regtimeend")) {
            params.put("regtimeEnd", param.getLong("regtimeend"));
        }
        return params;
    }

    @Override
    public JSONObject queryUser(int id) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean bean = accountMapper.getAccountById(id);
        JSONObject res = new JSONObject();
        if (bean == null) {
            setResult(res, false, "没有结果");
            return res;
        }
        setResult(res, true, "查询成功");
        res.put("user", bean);
        return res;
    }

    @Override
    public JSONObject doUserLogin(String uid, String pwd) {
        uid = uid.toLowerCase();
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        JSONObject res = new JSONObject();
        AccountBean accountBean = accountMapper.getAccountByUid(uid);
        if (accountBean == null) {
            setResult(res, false, "不存在的用户名");
            return res;
        }
        String pwdMd5 = StringUtil.createPwdMd5(uid, pwd);
        if (!accountBean.getPwd().equals(pwdMd5)) {
            setResult(res, false, "密码不正确");
            return res;
        }
        setResult(res, true, "登陆成功");
        res.put("user", accountBean);
        accountBean.setLastLogin(System.currentTimeMillis());
        accountMapper.update(accountBean);
        return res;
    }

    @Override
    public JSONObject queryForgetPasswordRequest(String uid, String email) {
        uid = uid.toLowerCase();
        email = email.toLowerCase();
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        JSONObject res = new JSONObject();
        AccountBean bean = accountMapper.getAccountByUid(uid);
        if (bean == null) {
            setResult(res, false, "不存在的用户名");
            return res;
        }
        if (!bean.getEmail().equals(email)) {
            setResult(res, false, "输入的邮箱和账号不匹配");
            return res;
        }
        long currentTime = System.currentTimeMillis();
        ResetCode resetCode = new ResetCode(ResetCode.TYPE_RESET_PWD, currentTime, uid);
        ClearTaskBean task = registerResetTask(bean, ClearTaskBean.TYPE_CLEAR_USER_RESETPWD, 3);
        task.setAccountBean(bean);
        boolean sendFlag = mailTemplate.sendResetPasswordEmail(bean, resetCode.generateResetCode());
        if (sendFlag) {
            setResult(res, true, "邮件已发送到" + email + "，请在3小时内确认操作");
            resetCodeTask.addTask(task);
        } else {
            setResult(res, false, "邮件发送失败，请稍后重试");
        }
        return res;
    }

    @Override
    public JSONObject queryResetEvent(String resetCode) {
        ClearTaskBean task = resetCodeTask.queryTask(resetCode);
        JSONObject res = new JSONObject();
        if (task == null) {
            setResult(res, false, "申请已失效，请重新申请或检查是否有更新的申请邮件");
            return res;
        }
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean account = accountMapper.getAccountById(task.getAccountBean().getId());
        if (account == null) {
            setResult(res, false, "不存在的用户，可能已被删除账号");
            return res;
        }
        setResult(res, true, "操作成功");
        res.put("user", account);
        return res;
    }

    @Override
    public JSONObject doForceChangePassword(String resetCode, String newPwd) {
        ClearTaskBean task = resetCodeTask.queryTask(resetCode);
        JSONObject res = new JSONObject();
        if (task == null) {
            setResult(res, false, "申请已失效，请重新申请或检查是否有更新的申请邮件");
            return res;
        }
        if (task.getResetCode().getCodeType() != ResetCode.TYPE_RESET_PWD) {
            setResult(res, false, "非法请求，请求码与任务类型不匹配");
            return res;
        }
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean bean = accountMapper.getAccountById(task.getAccountBean().getId());
        if (bean == null) {
            setResult(res, false, "不存在的用户，可能已被删除账号");
            return res;
        }
        String pwdMd5 = StringUtil.createPwdMd5(bean.getUid(), newPwd);
        bean.setPwd(pwdMd5);
        accountMapper.update(bean);
        setResult(res, true, "修改密码成功");
        return res;
    }

    @Override
    public JSONObject doConfirmEmail(String resetCode) {
        ClearTaskBean task = resetCodeTask.queryTask(resetCode);
        JSONObject res = new JSONObject();
        if (task == null) {
            setResult(res, false, "申请已失效，请重新申请或检查是否有更新的申请邮件");
            return res;
        }
        if (task.getResetCode().getCodeType() != ResetCode.TYPE_CONFIRM_EMAIL) {
            setResult(res, false, "非法请求，请求码与任务类型不匹配");
            return res;
        }
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean bean = accountMapper.getAccountById(task.getAccountBean().getId());
        if (bean == null) {
            setResult(res, false, "不存在的用户，可能账号已被删除");
            return res;
        }
        bean.setEmail(bean.getTempEmail());
        bean.setTempEmail(null);
        accountMapper.update(bean);
        setResult(res, true, "确认成功");
        return res;
    }

    @Override
    public JSONObject requestConfirmEmail(int id) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean account = accountMapper.getAccountById(id);
        JSONObject res = new JSONObject();
        if (account == null) {
            setResult(res, false, "不存在的用户，可能账号已被删除");
            return res;
        }
        if (account.getTempEmail() == null || !account.getTempEmail().matches("[0-9a-zA-Z_]+@[0-9a-zA-Z_.]")) {
            setResult(res, false, "用户没有待绑定的邮箱或邮箱格式不正确");
            return res;
        }
        String email = account.getTempEmail();
        ClearTaskBean task = registerResetTask(account, ClearTaskBean.TYPE_CLEAR_USER_CONFIRMEMAIL, 3);
        task.setAccountBean(account);
        if (mailTemplate.sendConfirmEmail(account, task.getResetCode().generateResetCode())) {
            resetCodeTask.addTask(task);
            setResult(res, true, "操作成功，请到" + email + "邮箱查收确认邮件");
        } else {
            setResult(res, false, "邮件发送失败，请稍后重试");
        }
        return res;
    }

    @Override
    public JSONObject doChangePwd(int id, String oldPwd, String newPwd) {
        JSONObject res = new JSONObject();
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean account = accountMapper.getAccountById(id);
        if (account == null) {
            setResult(res, false, "不存在的用户，账号可能已被删除");
            return res;
        }
        String oldPwdMd5 = StringUtil.createPwdMd5(account.getUid(), oldPwd);
        if (!oldPwdMd5.equals(account.getPwd())) {
            setResult(res, false, "旧密码不匹配");
            return res;
        }
        String pwdMd5 = StringUtil.createPwdMd5(account.getUid(), newPwd);
        account.setPwd(pwdMd5);
        accountMapper.update(account);
        setResult(res, true, "操作成功");
        return res;
    }

    @Override
    public JSONObject doChangeEmail(int id, String email) {
        email = email.toLowerCase();
        JSONObject res = new JSONObject();
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean accountBean = accountMapper.getAccountById(id);
        if (accountBean == null) {
            setResult(res, false, "不存在的用户，账号可能已被删除");
            return res;
        }
        if (accountMapper.getAccountByEmail(email) != null) {
            setResult(res, false, "邮箱已被注册");
            return res;
        }
        accountBean.setTempEmail(email);
        accountMapper.update(accountBean);
        //有绑定邮箱要先解绑
        if (accountBean.getEmail() != null) {
            ClearTaskBean task = registerResetTask(accountBean, ClearTaskBean.TYPE_CLEAR_USER_RESETEMAIL, 3);
            if (mailTemplate.sendResetEmailMail(accountBean, task.getResetCode().generateResetCode())) {
                resetCodeTask.addTask(task);
                setResult(res, true, "操作成功，请到" + accountBean.getEmail() + "邮箱先确认解绑操作");
                return res;
            }
        } else {//无绑定邮箱直接确认
            ClearTaskBean task = registerResetTask(accountBean, ClearTaskBean.TYPE_CLEAR_USER_CONFIRMEMAIL, 3);
            if (mailTemplate.sendConfirmEmail(accountBean, task.getResetCode().generateResetCode())) {
                resetCodeTask.addTask(task);
                setResult(res, true, "操作成功，请到" + accountBean.getTempEmail() + "邮箱进行认证绑定");
                return res;
            }
        }
        setResult(res, false, "邮件发送失败，请稍后尝试");
        return res;
    }

    @Override
    public JSONObject doClearTempEmail(int id, String email) {
        email = email.toLowerCase();
        JSONObject res = new JSONObject();
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean accountBean = accountMapper.getAccountById(id);
        if (accountBean != null && !accountBean.getTempEmail().equals(email)) {
            accountBean.setTempEmail(null);
            accountMapper.update(accountBean);
        }
        setResult(res, true, "操作成功");
        return res;
    }

    @Override
    public JSONObject doRegisterUser(String uid, String pwd, String nick, String email, String sex) {
        uid = uid.toLowerCase();
        if (email != null) {
            email = email.toLowerCase();
        }
        AccountBean account = new AccountBean();
        account.setTempEmail(email);
        account.setUid(uid);
        String pwdMd5 = StringUtil.createPwdMd5(uid, pwd);
        account.setPwd(pwdMd5);
        account.setSex(sex);
        account.setNick(nick);
        account.setRegTime(System.currentTimeMillis());
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        JSONObject res = new JSONObject();
        if (accountMapper.getAccountByUid(uid) != null) {
            setResult(res, false, "用户名已存在");
            return res;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("nick", nick);
        if (accountMapper.listAllAccounts(params, new RowBounds()).size() > 0) {
            setResult(res, false, "昵称已存在");
            return res;
        }
        if (email != null && accountMapper.getAccountByEmail(email) != null) {
            setResult(res, false, "邮箱已被注册");
            return res;
        }
        accountMapper.save(account);
        setResult(res, true, "注册成功，请尽快申请邮箱绑定");
        return res;
    }

    @Override
    public JSONObject doBindQQ(int id, String qq) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONObject doBindWeibo(int id, String weibo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONObject doQQLogin(String qq) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONObject doWeiboLogin(String weibo) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JSONObject editUser(int id, String pwd, String nick, String email, String sex, String avatar) {
        JSONObject res = new JSONObject();
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean account = accountMapper.getAccountById(id);
        if (account == null) {
            setResult(res, false, "不存在的用户");
            return res;
        }
        if (pwd != null) {
            String pwdMd5 = StringUtil.createPwdMd5(account.getUid(), pwd);
            account.setPwd(pwdMd5);
        }
        if (nick != null) {
            account.setNick(nick);
        }
        if (email != null) {
            account.setEmail(email.toLowerCase());
        }
        if (sex != null) {
            account.setSex(sex);
        }
        if (avatar != null) {
            account.setAvatar(avatar);
        }
        accountMapper.update(account);
        setResult(res, true, "修改成功");
        res.put("user", account);
        return res;
    }

    @Override
    public JSONObject doBanUser(int id, int days) {
        JSONObject res = new JSONObject();
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean account = accountMapper.getAccountById(id);
        if (account == null) {
            setResult(res, false, "不存在的用户");
            return res;
        }
        account.setBanFlag(true);
        accountMapper.update(account);
        setResult(res, true, "成功封禁用户");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        ClearTaskBean task = ClearTaskBean.generateUnbanUserTask(account, calendar.getTimeInMillis());
        resetCodeTask.addTask(task);
        return res;
    }

    @Override
    public void doUnbanUser(int id) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean bean = accountMapper.getAccountById(id);
        if (bean != null) {
            bean.setBanFlag(false);
            accountMapper.update(bean);
        }
    }

    @Override
    public void doUnbindQQ(int id) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean account = accountMapper.getAccountById(id);
        if (account != null) {
            account.setQq(null);
            accountMapper.update(account);
        }
    }

    @Override
    public void doUnbindWeibo(int id) {
        AccountMapper accountMapper = getMapper(AccountMapper.class);
        AccountBean account = accountMapper.getAccountById(id);
        if (account != null) {
            account.setWeibo(null);
            accountMapper.update(account);
        }
    }

    private ClearTaskBean registerResetTask(AccountBean user, int type, long delayHours) {
        ResetCode resetCode = ResetCodeFactory.generateResetCode(transferResetCodeType(type), user.getUid());
        long timeDelay = delayHours * 3600 * 1000;
        return new ClearTaskBean(resetCode, resetCode.getCreateTime() + timeDelay, user.getId(), type);
    }

}
