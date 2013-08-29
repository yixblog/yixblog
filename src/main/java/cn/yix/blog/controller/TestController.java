package cn.yix.blog.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-7-3
 * Time: 下午11:50
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/test_json.htm")
    public String testJSON(Model model){
        JSONObject res = new JSONObject();
        List<JSONObject> userList = new ArrayList<>();
        for (int i=0;i<10;i++){
            JSONObject user = new JSONObject();
            user.put("id",i);
            user.put("name","user"+i);
            userList.add(user);
        }
        res.put("users",userList);
        model.addAttribute("res",res);
        return "test/test_json";
    }
}
