package cn.yix.blog.controller;

import cn.yix.blog.utils.validate.ValidateCodeDesigner;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletResponse;
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
@SessionAttributes(SessionTokens.VALIDATE_TOKEN)
public class ValidateController {
    private Logger logger = Logger.getLogger(getClass());

    @RequestMapping("/img")
    public void generateValidateImage(ModelMap model, HttpServletResponse response) throws IOException {
        String validateCode = ValidateCodeDesigner.generateValidateCode(5);
        byte[] imageBytes = ValidateCodeDesigner.generateValidateImage(validateCode, 150, 40);
        model.addAttribute(SessionTokens.VALIDATE_TOKEN, validateCode);
        logger.debug("validate:" + validateCode);
        response.setHeader("Content-Type", "image/jpeg");
        OutputStream ous = response.getOutputStream();
        ous.write(imageBytes);
        ous.flush();
    }
}
