package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class ReplyReq {


    /**
     * uid : 0
     * username :
     * plid : 0
     * message :
     * token :
     */

    private int uid;
    private String username;
    private int plid;
    private String message;
    private String token;

    public ReplyReq(int uid, String username, int plid, String message, String token) {
        setMessage(message);
        setPlid(plid);
        setToken(token);
        setUid(uid);
        setUsername(username);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
