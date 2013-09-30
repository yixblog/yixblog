package cn.yix.blog.controller.exception;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-9-27
 * Time: 下午4:18
 */
public class ExceptionHandler extends AbstractHandlerExceptionResolver {
    private Logger logger = Logger.getLogger(getClass());

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestUri = request.getRequestURI();
        try {
            String outputString;
            logger.error(ex);
            ex.printStackTrace();
            if (requestUri.endsWith(".action")) {
                JSONObject resJSON = new JSONObject();
                resJSON.put("success", false);
                resJSON.put("msg", ex.getMessage());
                outputString = resJSON.toJSONString();
            } else {
                outputString = "发生异常:" + ex.getMessage();
            }
            response.getWriter().write(outputString);
            response.getWriter().flush();
        } catch (IOException ignored) {
        }
        return new ModelAndView();
    }
}
