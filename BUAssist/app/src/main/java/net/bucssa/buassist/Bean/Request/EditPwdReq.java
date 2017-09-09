package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class EditPwdReq {


    /**
     * uid : 0
     * oldpw :
     * newpw :
     * token :
     */

    private int uid;
    private String oldpw;
    private String newpw;
    private String token;

    public EditPwdReq(int uid, String oldpw, String newpw, String token) {
        setToken(token);
        setUid(uid);
        setOldpw(oldpw);
        setNewpw(newpw);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOldpw() {
        return oldpw;
    }

    public void setOldpw(String oldpw) {
        this.oldpw = oldpw;
    }

    public String getNewpw() {
        return newpw;
    }

    public void setNewpw(String newpw) {
        this.newpw = newpw;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
