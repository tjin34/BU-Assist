package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2018/4/8.
 */

public class DelSystemMsgReq {

    /**
     * uid : 1
     * nid : 1
     * token : 1
     */

    private int uid;
    private int nid;
    private String token;

    public DelSystemMsgReq(int uid, int nid, String token) {
        setUid(uid);
        setNid(nid);
        setToken(token);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
