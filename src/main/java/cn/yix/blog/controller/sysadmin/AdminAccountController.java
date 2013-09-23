package cn.yix.blog.controller.sysadmin;

import cn.yix.blog.controller.SessionTokens;
import cn.yix.blog.core.admin.IAdminAccountStorage;
import cn.yix.blog.utils.ResetCodeFactory;
import cn.yix.blog.utils.bean.ResetCode;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-28
 * Time: 上午11:39
 */
@Controller
@RequestMapping("/accountservice/adminaccount")
@SessionAttributes({SessionTokens.ADMIN_TOKEN, SessionTokens.VALIDATE_TOKEN})
public class AdminAccountController {
    @Resource(name = "adminAccountStorage")
    private IAdminAccountStorage adminAccountStorage;
    private Logger logger = Logger.getLogger(getClass());

    @RequestMapping(value = "/login.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject doLogin(@RequestParam String uid, @RequestParam String pwd, @RequestParam String validate, DefaultSessionAttributeStore status, WebRequest request, ModelMap modelMap) {
        String sessionValidate = (String) modelMap.remove(SessionTokens.VALIDATE_TOKEN);
        status.cleanupAttribute(request, SessionTokens.VALIDATE_TOKEN);
        if (sessionValidate != null && sessionValidate.equals(validate)) {
            JSONObject res = adminAccountStorage.doLogin(uid, pwd);
            logger.debug("admin login:" + uid);
            if (res.get("admin") != null) {
                modelMap.addAttribute("admin", res.get("admin"));
            }
            return res;
        }
        JSONObject res = new JSONObject();
        res.put("success", false);
        res.put("msg", "验证码不正确");
        return res;
    }

    @RequestMapping(value = "/login_status.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject loginStatus(ModelMap modelMap) {
        JSONObject admin = (JSONObject) modelMap.get("admin");
        JSONObject res = new JSONObject();
        if (admin != null) {
            res.put("admin", admin);
            res.put("success", true);
        } else {
            res.put("success", false);
        }
        return res;
    }

    @RequestMapping(value = "/admin/logout.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject logout(DefaultSessionAttributeStore status, WebRequest request, ModelMap modelMap) {
        JSONObject res = new JSONObject();
        modelMap.remove(SessionTokens.ADMIN_TOKEN);
        status.cleanupAttribute(request, SessionTokens.ADMIN_TOKEN);
        res.put("success", true);
        return res;
    }

    @RequestMapping("/reset/{resetCode}.htm")
    public String doReset(@PathVariable String resetCode, Model model) {
        ResetCode code = ResetCodeFactory.generateResetCode(resetCode);
        if (code != null) {
            switch (code.getCodeType()) {
                case ResetCode.TYPE_CONFIRM_EMAIL:
                    JSONObject json = adminAccountStorage.doConfirmEmail(resetCode);
                    model.addAttribute("res", json);
                    return "account/admin_confirm_email_res";
                case ResetCode.TYPE_RESET_PWD:
                    json = adminAccountStorage.queryResetCode(resetCode);
                    model.addAttribute("res", json);
                    return "account/admin_force_reset_pwd_dialog";
                case ResetCode.TYPE_RESET_EMAIL:
                    json = adminAccountStorage.queryResetCode(resetCode);
                    model.addAttribute("res", json);
                    return "account/admin_reset_email";
            }
        }
        return "redirect:/static/pages/illegal.html";
    }

    @RequestMapping("/forget_pwd.htm")
    public String forgetPwdDialog() {
        return "account/admin_forget_pwd_dialog";
    }

    @RequestMapping(value = "/forget_pwd_request.htm", method = RequestMethod.POST)
    public String forgetPwdRequest(@RequestParam String uid, @RequestParam String email, Model model) {
        JSONObject res = adminAccountStorage.queryForgetPasswordRequest(uid, email);
        model.addAttribute("res", res);
        return "account/forget_pwd_res";
    }

    @RequestMapping(value = "/force_reset_pwd.htm", method = RequestMethod.POST)
    public String forceResetPwd(@RequestParam String pwd, @RequestParam String resetcode, Model model) {
        JSONObject json = adminAccountStorage.doForceResetPassword(resetcode, pwd);
        model.addAttribute("res", json);
        return "account/reset_result";
    }

    @RequestMapping(value = "/reset_email.htm", method = RequestMethod.POST)
    public String resetEmail(@RequestParam String email, @RequestParam String resetcode, Model model) {
        JSONObject json = adminAccountStorage.doResetEmail(resetcode, email);
        model.addAttribute("res", json);
        return "account/admin_reset_email_result";
    }

    @RequestMapping(value = "/admin/save_admin.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject saveAdmin(@RequestParam String uid, @RequestParam String pwd, @RequestParam String email) {
        JSONObject res = new JSONObject();
        if (!uid.matches("[0-9a-zA-Z_]+")) {
            res.put("success", false);
            res.put("msg", "用户名中包含不合法字符");
        } else {
            res = adminAccountStorage.saveAdminAccount(uid, pwd, email);
        }
        return res;
    }

    @RequestMapping(value = "/admin/change_pwd.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @ModelAttribute("admin") JSONObject admin) {
        int adminId = admin.getIntValue("id");
        return adminAccountStorage.doChangePassword(adminId, oldPwd, newPwd);
    }

    @RequestMapping("/admin/reset_email.htm")
    public String resetEmailRequest(Model model, @ModelAttribute("admin") JSONObject admin) {
        int adminId = admin.getIntValue("id");
        JSONObject res = adminAccountStorage.queryResetEmailRequest(adminId);
        model.addAttribute("res", res);
        return "account/info";
    }

    @RequestMapping(value = "/admin/list.htm", method = RequestMethod.POST)
    public String listAdminAccounts(Model model, @ModelAttribute("admin") JSONObject admin,
                                    @RequestParam(required = false) String uid, @RequestParam(required = false) String email,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "20") int pageSize) {
        if (!admin.getBooleanValue("adminmanage")) {
            return "redirect:/static/pages/illegal.html";
        }
        JSONObject res = adminAccountStorage.queryAdminList(uid, email, page, pageSize);
        model.addAttribute("res", res);
        return "account/admin_list";
    }

    @RequestMapping(value = "/admin/detail.htm", method = RequestMethod.POST)
    public String adminDetail(Model model, @ModelAttribute("admin") JSONObject admin, @RequestParam int id) {
        if (!admin.getBooleanValue("adminmanage")) {
            return "redirect:/static/pages/illegal.html";
        }
        JSONObject res = adminAccountStorage.queryAdminById(id);
        model.addAttribute("res", res);
        return "account/admin_detail";
    }

    @RequestMapping(value = "/admin/delete_admin.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject deleteAdmin(@RequestParam int id, @ModelAttribute("admin") JSONObject admin) {
        JSONObject res = new JSONObject();
        if (id == admin.getIntValue("id")) {
            res.put("success", false);
            res.put("msg", "不能删除自己的账号");
            return res;
        }
        if (!admin.getBooleanValue("adminmanage")) {
            res.put("success", false);
            res.put("msg", "您没有权限这样做");
            return res;
        }
        return adminAccountStorage.deleteAdmin(id);
    }

    @RequestMapping("/login.htm")
    public String loginPage(ModelMap modelMap) {
        if (modelMap.get("admin") != null) {
            return "redirect:/account/adminaccount/admin/index.htm";
        }
        return "account/adminlogin";
    }

    @RequestMapping(value = "/admin/edit_admin.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject editAdmin(@RequestParam int id, @RequestParam String pwd, @RequestParam(required = false) String email, @ModelAttribute("admin") JSONObject admin) {
        JSONObject res = new JSONObject();
        if (!admin.getBooleanValue("adminmanage")) {
            res.put("success", false);
            res.put("msg", "您没有权限这样做");
            return res;
        }
        return adminAccountStorage.doEditAdmin(id, pwd, email);
    }
}
