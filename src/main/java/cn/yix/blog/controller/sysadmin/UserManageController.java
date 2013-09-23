package cn.yix.blog.controller.sysadmin;

import cn.yix.blog.controller.SessionTokens;
import cn.yix.blog.core.user.IUserAccountStorage;
import cn.yix.blog.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-16
 * Time: 下午11:14
 */
@RequestMapping("/sysadmin/user")
@SessionAttributes(SessionTokens.ADMIN_TOKEN)
@Controller
public class UserManageController {
    @Resource(name = "userAccountStorage")
    private IUserAccountStorage userAccountStorage;

    @RequestMapping("/list.htm")
    public String listUsers(@RequestParam(defaultValue = "1", required = false) int page,
                            @RequestParam(defaultValue = "20", required = false) int pageSize,
                            @ModelAttribute(SessionTokens.ADMIN_TOKEN) JSONObject admin, Model model) {
        if (!admin.getBooleanValue("accountmanage")) {
            return "redirect:/static/pages/illegal.html";
        }
        JSONObject params = new JSONObject();
        model.addAttribute("list", userAccountStorage.queryUsers(params, page, pageSize));
        return "admin/user_list";
    }

    @RequestMapping(value = "/query.htm",method = RequestMethod.POST)
    public String queryUser(@RequestParam(defaultValue = "1", required = false) int page,
                            @RequestParam(defaultValue = "20", required = false) int pageSize,
                            @RequestParam(required = false) String uid, @RequestParam(required = false) String email,
                            @RequestParam(required = false) String nick, @RequestParam(required = false) String qq,
                            @RequestParam(required = false) String weibo, @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate, @ModelAttribute(SessionTokens.ADMIN_TOKEN) JSONObject admin,
                            Model model) {
        if (!admin.getBooleanValue("accountmanage")) {
            return "redirect:/static/pages/illegal.html";
        }
        JSONObject params = new JSONObject();
        String dateFormat = "yyyy-MM-dd";
        if (uid != null) {
            params.put("uid", uid);
        }
        if (email != null) {
            params.put("email", email);
        }
        if (nick != null) {
            params.put("nick", nick);
        }
        if (qq != null) {
            params.put("qq", qq);
        }
        if (weibo != null) {
            params.put("weibo", weibo);
        }
        if (startDate != null) {
            params.put("regtimebegin", DateUtils.getTimeMillis(startDate, dateFormat));
        }
        if (endDate != null) {
            params.put("regtimeend", DateUtils.getTimeMillis(endDate, dateFormat));
        }
        model.addAttribute("list",userAccountStorage.queryUsers(params,page,pageSize));
        return "admin/user_list";
    }

    @RequestMapping(value = "/user_info.htm",method = RequestMethod.POST)
    public String getUserInfo(@RequestParam int id,@ModelAttribute(SessionTokens.ADMIN_TOKEN) JSONObject admin,Model model){
        if (!admin.getBooleanValue("accountmanage")) {
            return "redirect:/static/pages/illegal.html";
        }
        model.addAttribute("info",userAccountStorage.queryUser(id));
        return "admin/user_info";
    }

    @RequestMapping(value = "/edit_user.action",method = RequestMethod.POST)
    public @ResponseBody JSONObject editUser(@RequestParam int id,@RequestParam(required = false) String nick,
                                             @RequestParam(required = false) String pwd,
                                             @RequestParam(required = false) String email,
                                             @RequestParam(required = false) String sex,
                                             @ModelAttribute(SessionTokens.ADMIN_TOKEN) JSONObject admin){
        JSONObject res = new JSONObject();
        if (!admin.getBooleanValue("accountmanage")) {
            res.put("success",false);
            res.put("msg","您没有权限进行此操作");
            return res;
        }
        return userAccountStorage.editUser(id,pwd,nick,email,sex,null);
    }

    @RequestMapping(value = "/ban_user.action",method = RequestMethod.POST)
    public @ResponseBody JSONObject banUser(@RequestParam int id,@RequestParam int banDays,
                                            @ModelAttribute(SessionTokens.ADMIN_TOKEN) JSONObject admin){
        JSONObject res = new JSONObject();
        if (!admin.getBooleanValue("accountmanage")){
            res.put("success",false);
            res.put("msg","您没有权限执行此操作");
            return res;
        }
        return userAccountStorage.doBanUser(id,banDays);
    }
}
