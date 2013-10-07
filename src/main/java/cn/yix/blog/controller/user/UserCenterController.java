package cn.yix.blog.controller.user;

import cn.yix.blog.core.user.IUserAccountStorage;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-10-7
 * Time: 下午5:02
 */
@Controller
@RequestMapping("/userinfo")
public class UserCenterController {
    @Resource(name = "userAccountStorage")
    private IUserAccountStorage accountStorage;

    @RequestMapping("/{userid}.htm")
    public String centerPage(@PathVariable String userid, Model model) {
        if (userid.matches("\\d+")) {
            JSONObject queryRes = accountStorage.queryUser(Integer.parseInt(userid));
            if (queryRes.getBooleanValue("success")) {
                model.addAttribute("user", queryRes.getJSONObject("user"));
            }
        }
        return "account/user_info";
    }
}
