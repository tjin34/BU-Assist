package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class SendReq {


    /**
     * uid : 0
     * username :
     * touids : user1,user2
     * subject :
     * message :
     * token :
     */

    private int uid;
    private String username;
    private String touids;
    private String subject;
    private String message;
    private String token;

    public SendReq(int uid, String username, String touids, String subject, String message, String token) {
        setMessage(message);
        setSubject(subject);
        setToken(token);
        setTouids(touids);
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

    public String getTouids() {
        return touids;
    }

    public void setTouids(String touids) {
        this.touids = touids;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
