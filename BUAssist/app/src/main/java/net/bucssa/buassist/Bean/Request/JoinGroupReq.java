package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2017/12/21.
 */

public class JoinGroupReq {

    /**
     * uid : 0
     * groupid : 0
     * message :
     * token :
     */

    private int uid;
    private int groupid;
    private String message;
    private String token;

    public JoinGroupReq(int uid, int groupId, String message, String token) {
        setGroupid(groupId);
        setMessage(message);
        setToken(token);
        setUid(uid);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
