package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2017/12/21.
 */

public class AddClassCollectionReq {

    /**
     * uid : 0
     * classid : 0
     * token :
     */

    private int uid;
    private int classid;
    private String token;

    public AddClassCollectionReq(int uid, int classid, String token) {
        this.uid = uid;
        this.classid = classid;
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
