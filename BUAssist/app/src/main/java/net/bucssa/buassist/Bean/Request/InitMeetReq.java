package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2018/4/10.
 */

public class InitMeetReq {

    /**
     * uid : 0
     * groupId : 0
     * token :
     */

    private int uid;
    private int groupId;
    private String token;

    public InitMeetReq(int uid, int groupId, String token) {
        setGroupId(groupId);
        setUid(uid);
        setToken(token);
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
