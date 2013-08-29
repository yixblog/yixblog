package cn.yix.blog.spring;

import cn.yix.blog.core.admin.IAdminAccountStorage;
import cn.yix.blog.core.user.IUserAccountStorage;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-6-7
 * Time: 上午11:21
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
    private String[] adminBlackList;
    private String[] userBlackList;
    private boolean open;
    private Logger logger = Logger.getLogger(getClass());
    @Resource(name = "adminAccountStorage")
    private IAdminAccountStorage adminAccountStorage;
    @Resource(name = "userAccountStorage")
    private IUserAccountStorage userAccountStorage;

    public void setAdminBlackList(String[] adminBlackList) {
        this.adminBlackList = adminBlackList;
    }

    public void setUserBlackList(String[] userBlackList) {
        this.userBlackList = userBlackList;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!open) {
            return true;
        }
        HttpSession session = request.getSession();
        JSONObject admin = (JSONObject) session.getAttribute("admin");
        JSONObject user = (JSONObject) session.getAttribute("user");
        String uri = getRequestURI(request);
        if (admin == null) {
            for (String adminBlackpattern : adminBlackList) {
                if (checkUriMatch(uri, adminBlackpattern)) {
                    if (uri.endsWith(".action")) {
                        PrintWriter writer = response.getWriter();
                        writer.write(generateNotLoginJSON("您必须登录管理员账号后才能访问此页面").toJSONString());
                        writer.flush();
                        writer.close();
                    } else {
                        response.sendRedirect("static/pages/AdminNotLogin.html");
                    }
                    return false;
                }
            }
        } else {
            session.setAttribute("admin", adminAccountStorage.queryAdminById(admin.getIntValue("id")));
        }
        if (user == null) {
            for (String userBlackpattern : userBlackList) {
                if (checkUriMatch(uri, userBlackpattern)) {
                    if (uri.endsWith(".action")) {
                        PrintWriter writer = response.getWriter();
                        writer.write(generateNotLoginJSON("您必须登录后才能访问此页面").toJSONString());
                        writer.flush();
                        writer.close();
                    } else {
                        response.sendRedirect("static/pages/UserNotLogin.html");
                    }
                    return false;
                }
            }
        } else {
            JSONObject updatedUser = userAccountStorage.queryUser(user.getIntValue("id")).getJSONObject("user");
            logger.debug("session update user info:"+updatedUser.toJSONString());
            session.setAttribute("user", updatedUser);
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("post handle called,requestURI:"+request.getRequestURI()+",url:"+request.getRequestURL().toString());
        if (request.getRequestURI().endsWith(".htm")||request.getRequestURI().endsWith("/")) {
            logger.debug("called");
            String path = request.getContextPath();
            path = path.length()>0?path+"/":path;
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
            logger.debug("basepath:"+basePath);
            modelAndView.getModel().put("basePath", basePath);
//            logger.debug(((JSONObject)(modelAndView.getModel().get("data"))).toJSONString());
        }
        super.postHandle(request, response, handler, modelAndView);
    }

    private String getRequestURI(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctxPath = request.getContextPath();
        return uri.replace(ctxPath, "");
    }

    private boolean checkUriMatch(String uri, String pattern) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, uri);
    }

    private JSONObject generateNotLoginJSON(String msg) {
        JSONObject json = new JSONObject();
        json.put("success", false);
        json.put("msg", msg);
        logger.info("not login response:" + json.toJSONString());
        return json;
    }
}
