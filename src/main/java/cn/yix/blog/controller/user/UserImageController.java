package cn.yix.blog.controller.user;

import cn.yix.blog.controller.SessionTokens;
import cn.yix.blog.core.file.IImageListStorage;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-11-9
 * Time: 下午11:19
 */
@Controller
@RequestMapping("/user")
@SessionAttributes(SessionTokens.USER_TOKEN)
public class UserImageController {
    @Resource(name = "imageListStorage")
    private IImageListStorage imageListStorage;
    @RequestMapping(value = "/images.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject getUserImages(@RequestParam(defaultValue = "0", required = false) int page,
                             @RequestParam(defaultValue = "20", required = false) int pageSize,
                             @ModelAttribute(SessionTokens.USER_TOKEN) JSONObject user) {
        return imageListStorage.listUserImages(page,pageSize,user.getIntValue("id"));
    }
}
