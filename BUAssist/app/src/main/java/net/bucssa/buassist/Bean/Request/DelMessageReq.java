package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class DelMessageReq {

    /**
     * uid : 0
     * pmids : 3,5,20,40
     * token :
     */

    private int uid;
    private String pmids;
    private String token;

    public DelMessageReq(int uid, String pmids, String token) {
        setToken(token);
        setUid(uid);
        setPmids(pmids);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPmids() {
        return pmids;
    }

    public void setPmids(String pmids) {
        this.pmids = pmids;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
