package cn.yix.blog.controller.user;

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
@SessionAttributes("user")
public class ArticleEditController {
    @Resource(name = "articleStorage")
    private IArticleStorage articleStorage;

    @RequestMapping(value = "/save.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject addArticle(@RequestParam String title, @RequestParam String content, @RequestParam String tags, @ModelAttribute("user") JSONObject user) {
        int userId = user.getIntValue("id");
        String[] tagArray = splitTag(tags);
        return articleStorage.saveArticle(userId, title, content, false, tagArray);
    }

    private String[] splitTag(String tagString) {
        tagString = tagString.replace(" ", ",");
        tagString = tagString.replace(";", ",");
        tagString = tagString.replace("，", ",");
        tagString = tagString.replace("　", ",");
        return ",".split(tagString);
    }

    @RequestMapping(value = "/update.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject editArticle(@RequestParam int id, @RequestParam String title, @RequestParam String content, @RequestParam String tags, @ModelAttribute("user") JSONObject user) {
        int userId = user.getIntValue("id");
        String[] tagArray = splitTag(tags);
        return articleStorage.editArticle(userId, id, title, content, tagArray);
    }

    @RequestMapping(value = "/delete.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject deleteArticle(@RequestParam int id, @ModelAttribute("user") JSONObject user) {
        int userId = user.getIntValue("id");
        return articleStorage.deleteArticle(userId, id);
    }

    @RequestMapping("/new.htm")
    public String newArticlePage(Model model){

        return "article/new";
    }

    @RequestMapping(value = "/query.action",method = RequestMethod.POST)
    public @ResponseBody JSONObject querySelfArticles(@RequestParam(required = false) String tag,
                                                      @RequestParam(required = false,defaultValue = "1") int page,
                                                      @RequestParam(required = false,defaultValue = "15") int pageSize,
                                                      @ModelAttribute("user") JSONObject user){
        int userId = user.getIntValue("id");
        JSONObject res = articleStorage.queryArticles(page,pageSize,null,null,userId,tag,null,null);
        if ("".equals(tag)){
            tag=null;
        }
        res.put("tag",tag);
        return res;
    }

}
