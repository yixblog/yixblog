package cn.yix.blog.storage;

import cn.yix.blog.utils.bean.ResetCode;
import cn.yix.blog.utils.timertask.ClearTaskBean;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-21
 * Time: 下午4:50
 */
public abstract class AbstractStorage extends SqlSessionDaoSupport {

    protected void setResult(JSONObject res, boolean success, String msg) {
        res.put("success", success);
        res.put("msg", msg);
    }

    protected void setPageInfo(JSONObject res, int totalCount, int page, int pageSize) {
        res.put("totalcount", totalCount);
        res.put("totalpage", calculateTotalPage(totalCount, pageSize));
        res.put("pagesize",pageSize);
        res.put("page", page);
    }

    private int calculateTotalPage(int totalCount, int pageSize) {
        return (totalCount + pageSize - 1) / pageSize;
    }

    protected <T>T getMapper(Class<T> clazz){
        return getSqlSession().getMapper(clazz);
    }

    protected int transferResetCodeType(int type) {
        switch (type) {
            case ClearTaskBean.TPPE_CLEAR_ADMIN_RESETPWD:
            case ClearTaskBean.TYPE_CLEAR_USER_RESETPWD:
                return ResetCode.TYPE_RESET_PWD;
            case ClearTaskBean.TYPE_CLEAR_ADMIN_CONFIRMEMAIL:
            case ClearTaskBean.TYPE_CLEAR_USER_CONFIRMEMAIL:
                return ResetCode.TYPE_CONFIRM_EMAIL;
            case ClearTaskBean.TYPE_CLEAR_ADMIN_RESETEMAIL:
            case ClearTaskBean.TYPE_CLEAR_USER_RESETEMAIL:
                return ResetCode.TYPE_RESET_EMAIL;
            default:
                return -1;
        }
    }

    protected RowBounds getRowBounds(int page,int pageSize){
        return new RowBounds((page-1)*pageSize,pageSize);
    }
}
