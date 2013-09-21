package cn.yix.blog.controller.user;

import cn.yix.blog.core.user.IUserAccountStorage;
import cn.yix.blog.utils.ResetCodeFactory;
import cn.yix.blog.utils.bean.ResetCode;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-15
 * Time: 下午5:17
 */
@Controller
@RequestMapping("/accountservice/account")
@SessionAttributes("user")
public class UserAccountController {
    private static final String UID_REX = "[0-9A-Za-z_]+";
    @Resource(name = "userAccountStorage")
    private IUserAccountStorage userAccountStorage;
    private Logger logger = Logger.getLogger(getClass());

    @RequestMapping("/login.htm")
    public String loginPage() {
        return "account/userlogin";
    }

    @RequestMapping("/user/center.htm")
    public String userCenter() {
        return "account/user_center";
    }

    @RequestMapping(value = "/login.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject doLogin(@RequestParam String uid, @RequestParam String pwd, @RequestParam String validate, HttpSession session) {
        JSONObject res = new JSONObject();
        if (!uid.matches(UID_REX)) {
            res.put("success", false);
            res.put("msg", "用户名中包含非法字符");
            session.removeAttribute("validatecode");
            return res;
        }
        logger.debug("session obj id:" + session.getId());
        String sessValidate = (String) session.getAttribute("validatecode");
        session.removeAttribute("validatecode");
        if (sessValidate == null || !sessValidate.toLowerCase().equals(validate.toLowerCase())) {
            res.put("success", false);
            res.put("msg", "验证码不正确");
            return res;
        }
        res = userAccountStorage.doUserLogin(uid, pwd);
        if (res.getBooleanValue("success")) {
            session.setAttribute("user", res.getJSONObject("user"));
        }
        return res;
    }

    @RequestMapping(value = "/user/logout.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject logout(HttpSession session) {
        JSONObject user = (JSONObject) session.getAttribute("user");
        logger.info("user logout:" + user.getString("nick"));
        session.removeAttribute("user");
        JSONObject res = new JSONObject();
        res.put("success", true);
        res.put("msg", "成功退出");
        return res;
    }

    @RequestMapping(value = "/register.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject doRegister(@RequestParam String uid, @RequestParam String pwd,
                          @RequestParam(required = false) String nick,
                          @RequestParam(required = false) String email, @RequestParam String sex,
                          @RequestParam String validate, HttpSession session) {
        if (nick == null) {
            nick = uid;
        }
        JSONObject res = new JSONObject();
        String sessionValidate = (String) session.getAttribute("validatecode");
        logger.debug("session obj id:" + session.getId());
        logger.debug("session:" + sessionValidate + ",request:" + validate);
        session.removeAttribute("validatecode");
        if (sessionValidate == null || !sessionValidate.toLowerCase().equals(validate.toLowerCase())) {
            res.put("success", false);
            res.put("msg", "验证码不正确");
            session.removeAttribute("validatecode");
            return res;
        }
        if (!uid.matches(UID_REX)) {
            res.put("success", false);
            res.put("msg", "用户名中包含非法字符");
            session.removeAttribute("validatecode");
            return res;
        }
        switch (sex) {
            case "男":
            case "女":
            case "保密":
                break;
            default:
                sex = "保密";
        }
        return userAccountStorage.doRegisterUser(uid, pwd, nick, email, sex);
    }

    @RequestMapping("/register.htm")
    public String registerPage() {
        return "account/user_register";
    }

    @RequestMapping(value = "/forget_password.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject forgetPwdRequest(@RequestParam String uid, @RequestParam String email) {
        return userAccountStorage.queryForgetPasswordRequest(uid, email);
    }

    @RequestMapping("/forget_password_request.htm")
    public String forgetPwdPage() {
        return "account/forget_pwd_page";
    }

    @RequestMapping("/reset/{resetcode}.htm")
    public String queryResetCode(@PathVariable("resetcode") String resetCode, Model model) {
        ResetCode reset = ResetCodeFactory.generateResetCode(resetCode);
        if (reset != null) {
            switch (reset.getCodeType()) {
                case ResetCode.TYPE_CONFIRM_EMAIL:
                    JSONObject json = userAccountStorage.doConfirmEmail(resetCode);
                    model.addAttribute("res", json);
                    return "account/user_confirm_email_res";
                case ResetCode.TYPE_RESET_EMAIL:
                    json = userAccountStorage.queryResetEvent(resetCode);
                    model.addAttribute("res", json);
                    return "account/user_confirm_email";
                case ResetCode.TYPE_RESET_PWD:
                    json = userAccountStorage.queryResetEvent(resetCode);
                    model.addAttribute("res", json);
                    return "account/user_reset_pwd";
            }
        }
        return "redirect:/static/pages/illegal.html";
    }

    @RequestMapping(value = "/user/reset_email.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject resetEmail(@RequestParam String email, @ModelAttribute("user") JSONObject user) {
        return userAccountStorage.doChangeEmail(user.getIntValue("id"), email);
    }

    @RequestMapping(value = "/reset_pwd.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject resetPwd(@RequestParam String pwd, @RequestParam String resetCode) {
        return userAccountStorage.doForceChangePassword(resetCode, pwd);
    }

    @RequestMapping("/user/reset_email.htm")
    public String resetEmailRequest(@ModelAttribute("user") JSONObject user, Model model) {
        model.addAttribute("user", user);
        return "account/user_reset_email";
    }

    @RequestMapping(value = "/user/confirm_email.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject confirmEmail(@ModelAttribute("user") JSONObject user) {
        return userAccountStorage.requestConfirmEmail(user.getIntValue("id"));
    }

    @RequestMapping(value = "/user/change_pwd.htm")
    public String changePwdPage() {
        return "account/change_user_pwd";
    }

    @RequestMapping(value = "/user/change_pwd.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @ModelAttribute("user") JSONObject user) {
        return userAccountStorage.doChangePwd(user.getIntValue("id"), oldPwd, newPwd);
    }

    @RequestMapping("/user/change_info.htm")
    public String changeInfoPage(@ModelAttribute("user") JSONObject user, Model model) {
        model.addAttribute("user", user);
        return "account/user_change_info_page";
    }

    @RequestMapping(value = "/user/change_info.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject doChangeInfo(@RequestParam String nick, @RequestParam String sex, @ModelAttribute("user") JSONObject user) {
        JSONObject res = userAccountStorage.editUser(user.getIntValue("id"), null, nick, null, sex, null);
        if (res.getBooleanValue("success")) {
            user.put("nick", nick);
            user.put("sex", sex);
        }
        return res;
    }

    @RequestMapping("/user/change_avatar.htm")
    public String changeAvatarPage(@ModelAttribute("user") JSONObject user, Model model) {
        model.addAttribute("user", user);
        return "account/user_change_avatar";
    }

    @RequestMapping(value = "/user/change_avatar.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject doChangeAvatar(@RequestParam String avatar, @ModelAttribute("user") JSONObject user) {
        JSONObject res = userAccountStorage.editUser(user.getIntValue("id"), null, null, null, null, avatar);
        if (res.getBooleanValue("success")) {
            user.put("avatar", avatar);
        }
        return res;
    }

    @RequestMapping("/user/bind.htm")
    public String bindPage(@ModelAttribute("user") JSONObject user, Model model) {
        model.addAttribute("user", user);
        return "account/bind";
    }

}
