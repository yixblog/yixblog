package cn.yix.blog.controller.ueditor;

import cn.yix.blog.core.file.IImageListStorage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    String manageImages() {
        List<String> files = imageListStorage.listAllImages();
        return connectFiles(files);
    }

    private String connectFiles(List<String> files) {
        StringBuilder res = new StringBuilder();
        for (String file : files) {
            res.append(file).append("ue_separate_ue");
        }
        return res.length() > 0 ? res.substring(0, res.lastIndexOf("ue_separate_ue")) : "";
    }
}
