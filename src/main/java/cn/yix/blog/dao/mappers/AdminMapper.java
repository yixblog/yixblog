package cn.yix.blog.dao.mappers;

import cn.yix.blog.dao.beans.AdminBean;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-11
 * Time: 上午9:14
 */
public interface AdminMapper {
    public AdminBean getAdminByUid(String uid);

    public AdminBean getAdminById(int id);

    public List<AdminBean> listAdmins(Map<String,Object> params,RowBounds pageArgs);

    public int countAdmins(Map<String,Object> params);

    public void save(AdminBean adminBean);

    public void update(AdminBean adminBean);

    public void delete(AdminBean adminBean);

    public AdminBean getAdminByEmail(String email);

}
