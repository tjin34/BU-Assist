package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2017/12/21.
 */

public class LeaveGroupReq {

    /**
     * uid : 0
     * groupid : 0
     * token :
     */

    private int uid;
    private int groupid;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
