package cn.yix.blog.storage.article.datafix;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-30
 * Time: 下午9:22
 */
public class ArticleQueryJSON extends JSONObject {
    private Logger logger = Logger.getLogger(getClass());

    @Override
    public Object get(Object key) {
        logger.debug("query key------------------------------" + key);
        String keyString = (String) key;
        if (keyString.matches("__frch_item_\\d+")) {
            int index = getFrchItemIndex(keyString);
            return super.getJSONArray("keywords").get(index);
        }
        return super.get(key);
    }

    private int getFrchItemIndex(String keyString) {
        String indexString = keyString.replace("__frch_item_", "");
        return Integer.parseInt(indexString);
    }
}
