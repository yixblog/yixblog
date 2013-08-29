package cn.yix.blog.dao.mappers;

import cn.yix.blog.dao.beans.NoticeBean;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-18
 * Time: 下午9:33
 */
public interface NoticeMapper {
    public List<NoticeBean> list(RowBounds pageArgs);

    public int count();

    public NoticeBean queryById(int id);

    public void save(NoticeBean bean);

    public void update(NoticeBean bean);

    public void delete(int id);
}
