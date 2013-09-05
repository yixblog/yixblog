package cn.yix.blog.controller.ueditor;

import cn.yix.blog.core.file.IFileSavingStorage;
import cn.yix.blog.core.file.SavingResultInfo;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-8-31
 * Time: 上午11:52
 */
@Controller
@RequestMapping("/uploader")
public class FileUploadController {
    private static final String SAVE_PATH = "upload";
    private Logger logger = Logger.getLogger(getClass());

    @Resource(name = "fileSavingStorage")
    private IFileSavingStorage fileSavingStorage;

    @RequestMapping(value = "/image.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject uploadImage(@RequestParam MultipartFile upfile, @RequestParam String pictitle) throws Exception {
        String[] fileType = {".gif", ".png", ".jpg", ".jpeg"};
        int maxSize = 10000;
        SavingResultInfo up = fileSavingStorage.saveFile(upfile, pictitle, fileType, maxSize);
        logger.debug("image uploaded,file size:" + up.getSize() + ",url:" + up.getUrl());
        return buildUploadResult(up);
    }

    @RequestMapping(value = "/file.action", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject uploadFile(@RequestParam MultipartFile upfile, @RequestParam(required = false) String pictitle) {
        String[] fileType = {".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".xls", ".xlsx", ".jar"};  //允许的文件类型
        int maxSize = 10000;
        SavingResultInfo up = fileSavingStorage.saveFile(upfile, pictitle, fileType, maxSize);
        logger.debug("file uploaded,file size:" + up.getSize() + ",url:" + up.getUrl());
        return buildUploadResult(up);
    }

    @RequestMapping(value = "/scrawl.action", method = RequestMethod.POST, params = "action=tmpImg")
    public
    @ResponseBody
    String uploadScrawl(@RequestParam(required = false) String pictitle, @RequestParam(required = false) MultipartFile upfile) {
        String[] fileType = {".gif", ".png", ".jpg", ".jpeg"};
        int maxSize = 10000;
        SavingResultInfo up = fileSavingStorage.saveFile(upfile, pictitle, fileType, maxSize);
        return "<script>parent.ue_callback('" + up.getUrl() + "','" + up.getState() + "')</script>";
    }

    @RequestMapping(value = "/scrawl.action",method = RequestMethod.POST,params = "content")
    public @ResponseBody JSONObject uploadScrawl2(@RequestParam String content){
        String[] fileType = {".gif", ".png", ".jpg", ".jpeg"};
        int maxSize = 10000;
        SavingResultInfo up = fileSavingStorage.saveBase64(content,fileType,maxSize);
        return buildUploadResult(up);
    }

    private JSONObject buildUploadResult(SavingResultInfo up) {
        JSONObject res = new JSONObject();
        res.put("original", up.getOriginalName());
        res.put("url", up.getUrl());
        res.put("title", up.getTitle());
        res.put("state", up.getState());
        return res;
    }

}
