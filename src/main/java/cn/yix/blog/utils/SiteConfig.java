package cn.yix.blog.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-27
 * Time: 上午8:47
 */
@Service("siteConfig")
public class SiteConfig {
    @Value("${site.domain}")
    private String domain;

    public String getDomain() {
        return domain;
    }

}
