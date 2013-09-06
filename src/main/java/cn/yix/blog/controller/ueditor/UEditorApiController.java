package cn.yix.blog.controller.ueditor;

import cn.yix.blog.core.file.IImageListStorage;
import cn.yix.blog.utils.UEditorConfig;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-1
 * Time: 下午9:54
 */
@Controller
@RequestMapping("/ueditor")
@SessionAttributes("user")
public class UEditorApiController {
    @Resource(name = "imageListStorage")
    private IImageListStorage imageListStorage;

    @RequestMapping(value = "/movie.action", method = RequestMethod.POST)
    public
    @ResponseBody
    String getMovie(@RequestParam String searchKey, @RequestParam String videoType) throws IOException {
        searchKey = URLEncoder.encode(searchKey, "utf-8");
        URL url = new URL("http://api.tudou.com/v3/gw?method=item.search&appKey=myKey&format=json&kw=" + searchKey + "&pageNo=1&pageSize=20&channelId=" + videoType + "&inDays=7&media=v&sort=s");
        URLConnection conn = url.openConnection();
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    @RequestMapping(value = "/imageManage.action", method = RequestMethod.POST)
    public
    @ResponseBody
    String manageImages(@ModelAttribute("user")JSONObject user) {
        List<String> files = imageListStorage.listAllImages(user.getIntValue("id"));
        return UEditorConfig.combineUrls(files);
    }

}
