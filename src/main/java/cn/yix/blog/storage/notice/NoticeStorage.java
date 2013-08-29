package cn.yix.blog.storage.notice;

import cn.yix.blog.core.notice.INoticeStorage;
import cn.yix.blog.dao.beans.NoticeBean;
import cn.yix.blog.dao.mappers.NoticeMapper;
import cn.yix.blog.storage.AbstractStorage;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-6-26
 * Time: 下午4:42
 */
@Repository("noticeStorage")
public class NoticeStorage extends AbstractStorage implements INoticeStorage {
    @Resource(name = "sessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Override
    public JSONObject queryNotices(int page, int pageSize) {
        NoticeMapper noticeMapper = getMapper(NoticeMapper.class);
        List<NoticeBean> notices = noticeMapper.list(getRowBounds(page, pageSize));
        int total = noticeMapper.count();
        JSONObject res = new JSONObject();
        setResult(res, true, "查询成功");
        setPageInfo(res, total, page, pageSize);
        res.put("notices", notices);
        return res;
    }

    @Override
    public JSONObject queryNotice(int noticeId) {
        NoticeMapper noticeMapper = getMapper(NoticeMapper.class);
        NoticeBean notice = noticeMapper.queryById(noticeId);
        JSONObject res = new JSONObject();
        if (notice == null) {
            setResult(res, false, "不存在的通告");
            return res;
        }
        setResult(res, true, "查询成功");
        res.put("notice", notice);
        return res;
    }

    @Override
    public JSONObject saveNotice(String title, String content) {
        NoticeMapper mapper = getMapper(NoticeMapper.class);
        NoticeBean notice = new NoticeBean();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setAddTime(System.currentTimeMillis());
        mapper.save(notice);
        JSONObject res = new JSONObject();
        setResult(res, true, "保存成功");
        res.put("notice", notice);
        return res;
    }

    @Override
    public JSONObject updateNotice(int noticeId, String title, String content) {
        NoticeMapper noticeMapper = getMapper(NoticeMapper.class);
        JSONObject res = new JSONObject();
        NoticeBean notice = noticeMapper.queryById(noticeId);
        if (notice == null) {
            setResult(res, false, "不存在的通告");
            return res;
        }
        notice.setTitle(title);
        notice.setContent(content);
        noticeMapper.update(notice);
        setResult(res, true, "修改成功");
        return res;
    }

    @Override
    public JSONObject deleteNotice(int noticeId) {
        NoticeMapper noticeMapper = getMapper(NoticeMapper.class);
        JSONObject res = new JSONObject();
        noticeMapper.delete(noticeId);
        setResult(res, true, "删除成功");
        return res;
    }
}
