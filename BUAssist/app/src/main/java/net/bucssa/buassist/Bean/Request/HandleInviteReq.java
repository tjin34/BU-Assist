package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2017/12/21.
 */

public class HandleInviteReq {

    /**
     * nid : 0
     * uid : 0
     * groupid : 0
     * result : 0
     * token :
     */

    private int nid;
    private int uid;
    private int groupid;
    private int result;
    private String token;

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
