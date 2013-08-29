package cn.yix.blog.core.admin;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-4
 * Time: 下午12:31
 */
public interface IAdminCharacterStorage {
    public List<JSONObject> queryAllCharacters();

    /**
     * update character name and permission
     * @param id character id
     * @param name character name,if not change,keep null
     * @param permissionMask mask:binary:|adminManage|articleManage|commentManage|systemManage|userManage|
     * @return result JSON
     */
    public JSONObject updateCharacter(int id,String name,int permissionMask);

    /**
     * add new character
     * @param name name
     * @param permissionMask mask:binary:|adminManage|articleManage|commentManage|systemManage|userManage|
     * @return result JSON
     */
    public JSONObject saveCharacter(String name,int permissionMask);

    public JSONObject deleteCharacter(int id);

    public JSONObject queryCharacter(int id);

    public JSONObject doSetAdminCharacters(int adminId,int... characterIds);
}
