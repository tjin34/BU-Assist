package net.bucssa.buassist.Bean.Request;

/**
 * Created by tjin3 on 2017/12/21.
 */

public class EmailVerifyReq {

    /**
     * uid : 0
     * token :
     */

    private int uid;
    private String token;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
