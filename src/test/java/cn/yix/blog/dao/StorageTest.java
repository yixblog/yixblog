package cn.yix.blog.dao;

import cn.yix.blog.core.admin.IAdminAccountStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-27
 * Time: 下午7:56
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:WEB-INF/applicationContext.xml")
public class StorageTest {
    @Resource(name = "adminAccountStorage")
    private IAdminAccountStorage adminAccountStorage;
    @Test
    public void testStorage(){
        while (true){
            adminAccountStorage.queryResetCode("asdfasdf");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}
