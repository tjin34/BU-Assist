package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class DelCollectionReq {

    /**
     * uid : 0
     * tids : 0
     * token :
     */

    private int uid;
    private int tids;
    private String token;

    public DelCollectionReq(int uid, int tids, String token) {
        setToken(token);
        setTid(tids);
        setUid(uid);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTids() {
        return tids;
    }

    public void setTid(int tids) {
        this.tids = tids;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
