package cn.yix.blog.controller;

import cn.yix.blog.core.article.IArticleStorage;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-31
 * Time: 上午12:43
 */
@Controller
@RequestMapping("/")
public class IndexController {

    private Logger logger = Logger.getLogger(getClass());
    @Resource(name = "articleStorage")
    private IArticleStorage articleStorage;

    @RequestMapping({"/index.htm","/"})
    public String showIndex(Model model,HttpSession session) {
        JSONObject data = new JSONObject();
        JSONObject newArticles = articleStorage.queryArticles(1, 8, null, null, 0, null, null, "addtime");
        logger.debug("newArticle:"+newArticles.toJSONString());
        data.put("newArticles", newArticles);
        JSONObject hotArticles = articleStorage.queryArticles(1, 8, null, null, 0, null, null, "replycount");
        logger.debug("hotArticle:"+hotArticles.toJSONString());
        data.put("hotArticles", hotArticles);
        data.put("sitename", "佚博客");
        model.addAttribute("data", data);
        logger.debug(data.toJSONString());
        return "index";
    }

}
