package cn.yix.blog.controller;

import cn.yix.blog.utils.validate.ValidateCodeDesigner;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-18
 * Time: 下午10:59
 */
@Controller
@RequestMapping("/**/validate")
public class ValidateController {
    private Logger logger = Logger.getLogger(getClass());
    @RequestMapping("/img")
    public void generateValidateImage(HttpSession session,HttpServletResponse response) throws IOException {
        String validateCode = ValidateCodeDesigner.generateValidateCode(5);
        byte[] imageBytes = ValidateCodeDesigner.generateValidateImage(validateCode, 150, 40);
        session.setAttribute("validatecode", validateCode);
        logger.debug("session obj id:"+session.getId());
        logger.debug("validate:"+validateCode);
        response.setHeader("Content-Type","image/jpeg");
        OutputStream ous = response.getOutputStream();
        ous.write(imageBytes);
        ous.flush();
    }
}
