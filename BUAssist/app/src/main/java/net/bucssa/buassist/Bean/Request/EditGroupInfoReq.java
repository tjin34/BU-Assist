package net.bucssa.buassist.Bean.Request;

/**
 * Created by shinji on 2018/4/13.
 */

public class EditGroupInfoReq {

    /**
     * uid : 0
     * groupId : 0
     * key :
     * value :
     * token :
     */

    private int uid;
    private int groupId;
    private String key;
    private String value;
    private String token;

    public EditGroupInfoReq(int uid, int groupId, String key, String value, String token) {
        setGroupId(groupId);
        setKey(key);
        setToken(token);
        setUid(uid);
        setValue(value);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
