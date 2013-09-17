package cn.yix.blog.controller.comment;

import cn.yix.blog.core.comment.ICommentStorage;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-17
 * Time: 下午11:45
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
    @Resource(name = "commentStorage")
    private ICommentStorage commentStorage;
    @RequestMapping(value = "/list.action",method = RequestMethod.POST)
    public @ResponseBody
    JSONObject listComments(@RequestParam int articleId,@RequestParam(defaultValue = "1",required = false) int page,@RequestParam(defaultValue = "10",required = false) int pageSize){
        return commentStorage.queryArticleComments(articleId,page,pageSize);
    }
}
