package cn.yix.blog.controller.user;

import cn.yix.blog.core.comment.ICommentStorage;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-7-30
 * Time: 下午10:12
 */
@Controller
@RequestMapping("/user/comments")
@SessionAttributes("user")
public class UserCommentsController {
    @Resource(name = "commentStorage")
    private ICommentStorage commentStorage;

    @RequestMapping(value = "/my_comments.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject getMyComments(@ModelAttribute("user") JSONObject user, @RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return commentStorage.queryUserComments(user.getIntValue("id"), page, pageSize);
    }

    @RequestMapping(value = "/comments_to_me.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject getCommentsToMe(@ModelAttribute("user") JSONObject user, @RequestParam(required = false, defaultValue = "1") int page,
                               @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return commentStorage.queryCommentsToUser(user.getIntValue("id"), page, pageSize);
    }
}
