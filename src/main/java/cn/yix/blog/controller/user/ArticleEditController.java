package cn.yix.blog.controller.user;

import cn.yix.blog.controller.SessionTokens;
import cn.yix.blog.core.article.IArticleStorage;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-6-27
 * Time: 下午8:41
 * <p/>
 * controller dealing article add,update request,require user login
 */
@Controller
@RequestMapping("/user/article")
@SessionAttributes(SessionTokens.USER_TOKEN)
public class ArticleEditController {
    @Resource(name = "articleStorage")
    private IArticleStorage articleStorage;

    @RequestMapping(value = "/save.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject addArticle(@RequestParam String title, @RequestParam String content, @RequestParam String tags, @ModelAttribute(SessionTokens.USER_TOKEN) JSONObject user) {
        int userId = user.getIntValue("id");
        String[] tagArray = tags.split(",");
        return articleStorage.saveArticle(userId, title, content, false, tagArray);
    }

    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject editArticle(@RequestParam int id, @RequestParam String title, @RequestParam String content, @RequestParam String tags, @ModelAttribute(SessionTokens.USER_TOKEN) JSONObject user) {
        int userId = user.getIntValue("id");
        String[] tagArray = tags.split(",");
        return articleStorage.editArticle(userId, id, title, content, tagArray);
    }

    @RequestMapping(value = "/delete.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject deleteArticle(@RequestParam int id, @ModelAttribute(SessionTokens.USER_TOKEN) JSONObject user) {
        int userId = user.getIntValue("id");
        return articleStorage.deleteArticle(userId, id);
    }

    @RequestMapping("/{article_id}/edit.htm")
    public String editArticlePage(Model model, @ModelAttribute(SessionTokens.USER_TOKEN) JSONObject user, @PathVariable("article_id") int articleId) {
        JSONObject res = articleStorage.queryArticleForEdit(articleId, user.getIntValue("id"));
        model.addAttribute("res", res);
        return "article/edit";
    }

    @RequestMapping("/new.htm")
    public String newArticlePage(Model model, @ModelAttribute(SessionTokens.USER_TOKEN) JSONObject user) {
        JSONObject tags = articleStorage.getUserTags(user.getIntValue("id"));
        model.addAttribute("tags", tags);
        return "article/new";
    }

    @RequestMapping(value = "/query.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject querySelfArticles(@RequestParam(required = false) String tag,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "15") int pageSize,
                                 @ModelAttribute(SessionTokens.USER_TOKEN) JSONObject user) {
        int userId = user.getIntValue("id");
        JSONObject res = articleStorage.queryArticles(page, pageSize, null, null, userId, tag, null, null);
        if ("".equals(tag)) {
            tag = null;
        }
        res.put("tag", tag);
        return res;
    }

}
