package cn.yix.blog.dao.mappers;

import cn.yix.blog.dao.beans.CharacterBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 上午12:52
 */
public interface CharacterMapper {
    public List<CharacterBean> listCharacters();

    public List<CharacterBean> listAdminCharacters(int adminId);

    public void save(CharacterBean character);

    public void update(CharacterBean character);

    public void delete(CharacterBean character);

    public CharacterBean getCharacter(int id);

    public int checkNameExists(String name);

    public void clearAdminCharacters(int adminId);

    public void setAdminCharacter(int adminId,int characterId);

}
