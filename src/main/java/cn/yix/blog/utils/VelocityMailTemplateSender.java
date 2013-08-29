package cn.yix.blog.utils;

import cn.yix.blog.dao.beans.AccountBean;
import cn.yix.blog.dao.beans.AdminBean;
import cn.yix.blog.dao.beans.DatabaseEntity;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 下午10:41
 */
@Component("mailTemplate")
public class VelocityMailTemplateSender {
    protected final Logger logger = Logger.getLogger(getClass());
    @Resource(name = "javaMailSender")
    private JavaMailSender javaMailSender;
    @Resource(name = "siteConfig")
    private SiteConfig siteConfig;
    private VelocityEngine velocityEngine;
    @Resource(name = "velocityConfigurer")
    private void setVelocityConfigurer(VelocityConfigurer velocityConfigurer){
        velocityEngine = velocityConfigurer.getVelocityEngine();
    }

    public synchronized boolean sendEmail(String title, String target, String vmPath, Map<String, Object> params) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            helper.setTo(target);
            helper.setSubject(title);
            helper.setText(getMessage(vmPath, params), true);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            logger.error(e);
            logger.info("邮件发送异常，title:" + title + ",target:" + target);
            return false;
        } catch (MailException e){
            logger.error(e);
            logger.info("邮件发送异常，title:" + title + ",target:" + target);
            throw e;
        }
        return true;
    }

    private String getMessage(String vmPath, Map<String, Object> model) {
        final String encoding = "utf-8";
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, vmPath, encoding, model);
    }

    public boolean sendResetPasswordEmail(DatabaseEntity accountBean,String resetcode) {
        final String title = "佚博客：密码重置";
        final String vmPath = "mail/resetpassword.vm";
        Map<String, Object> params = new HashMap<>();
        String nick = null;
        String uid = null;
        String type = "account";
        String email = null;
        params.put("title", title);
        if (accountBean instanceof AccountBean) {
            AccountBean bean = (AccountBean) accountBean;
            nick = bean.getNick();
            uid = bean.getUid();
            email = bean.getEmail().split(",")[0];
        } else if (accountBean instanceof AdminBean) {
            AdminBean bean = (AdminBean) accountBean;
            uid = nick = bean.getUid();
            type = "adminaccount";
            email = bean.getEmail().split(",")[0];
        }
        String url = siteConfig.getDomain() + "/accountservice/" + type + "/reset/" + resetcode+".htm";
        params.put("url",url);
        params.put("nick",nick);
        params.put("uid",uid);
        return sendEmail(title, email, vmPath, params);
    }

    public boolean sendResetEmailMail(DatabaseEntity bean,String resetCode) {
        final String title = "佚博客：修改邮件";
        final String vmPath = "mail/resetemail.vm";
        Map<String,Object> params = new HashMap<>();
        String nick = null;
        String type="account";
        String uid = null;
        String email = null;
        params.put("title",title);
        if (bean instanceof AdminBean){
            AdminBean adminBean = (AdminBean)bean;
            uid = nick = adminBean.getUid();
            type="adminaccount";
            email = adminBean.getEmail();
        }else if(bean instanceof AccountBean){
            AccountBean accountBean = (AccountBean)bean;
            uid = accountBean.getUid();
            nick = accountBean.getNick();
            email = accountBean.getEmail();
        }
        String url = siteConfig.getDomain()+"/accountservice/"+type+"/reset/"+resetCode+".htm";
        params.put("url",url);
        params.put("nick",nick);
        params.put("uid",uid);
        return sendEmail(title,email,vmPath,params);
    }

    public boolean sendConfirmEmail(DatabaseEntity bean,String resetCode) {
        final String title = "佚博客：确认邮箱";
        final String vmPath = "mail/confirmemail.vm";
        Map<String,Object> params = new HashMap<>();
        String nick = null;
        String type="account";
        String uid = null;
        String email = null;
        params.put("title",title);
        if (bean instanceof AdminBean){
            AdminBean adminBean = (AdminBean)bean;
            uid = nick = adminBean.getUid();
            type="adminaccount";
            email = adminBean.getTempEmail();
        }else if(bean instanceof AccountBean){
            AccountBean accountBean = (AccountBean)bean;
            uid = accountBean.getUid();
            nick = accountBean.getNick();
            email = accountBean.getTempEmail();
        }
        String url = siteConfig.getDomain()+"/accountservice/"+type+"/reset/"+resetCode+".htm";
        params.put("url",url);
        params.put("nick",nick);
        params.put("uid",uid);
        return sendEmail(title,email,vmPath,params);
    }
}
