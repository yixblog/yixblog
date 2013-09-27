package cn.yix.blog.dao.mappers;

import cn.yix.blog.dao.beans.AccountBean;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-23
 * Time: 下午9:46
 */
public interface AccountMapper {
    /**
     * list available accounts
     *
     * @param params   query mapper,available key:uid,nick,qq,weibo,email,regtimeBegin,regtimeEnd
     * @param pageArgs page args
     * @return AccountBean list
     */
    public List<AccountBean> listAccounts(Map<String, Object> params, RowBounds pageArgs);

    public int countListAccounts(Map<String, Object> params);

    //list all accounts includes banned ones
    public List<AccountBean> listAllAccounts(Map<String, Object> params, RowBounds pageArgs);

    public int countListAllAccounts(Map<String, Object> params);

    public AccountBean getAccountById(int id);

    public AccountBean getAccountByUid(String uid);

    public AccountBean getAccountByQQ(String qq);

    public AccountBean getAccountByWeibo(String weibo);

    public AccountBean getAccountByEmail(String email);

    public void save(AccountBean accountBean);

    public void update(AccountBean accountBean);

    public List<AccountBean> listTopArticleAuthors(RowBounds rb);
}
