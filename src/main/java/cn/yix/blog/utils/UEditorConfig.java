package cn.yix.blog.utils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-6
 * Time: 下午7:10
 */
public class UEditorConfig {
    public static final String UE_SEPERATOR = "ue_separate_ue";

    public static String combineUrls(List<String> files) {
        StringBuilder res = new StringBuilder();
        for (String file : files) {
            res.append(file).append("ue_separate_ue");
        }
        return res.length() > 0 ? res.substring(0, res.lastIndexOf(UEditorConfig.UE_SEPERATOR)) : "";
    }

}
