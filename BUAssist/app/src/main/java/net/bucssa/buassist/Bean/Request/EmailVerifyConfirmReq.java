package net.bucssa.buassist.Bean.Request;

/**
 * Created by tjin3 on 2017/12/21.
 */

public class EmailVerifyConfirmReq {

    /**
     * uid : 0
     * authCode :
     * token :
     */

    private int uid;
    private String authCode;
    private String token;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
