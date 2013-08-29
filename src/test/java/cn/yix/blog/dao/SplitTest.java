package cn.yix.blog.dao;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-6-27
 * Time: 下午9:34
 */
public class SplitTest {
    @Test
    public void testSplit(){
        System.out.println(ArrayUtils.toString("abc,bcd".split(",")));
    }
}
