package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class DelFriendReq {


    /**
     * uid : 0
     * friendid : 0
     * token :
     */

    private int uid;
    private int friendid;
    private String token;

    public DelFriendReq(int uid, int friendid, String token) {
        setToken(token);
        setUid(uid);
        setFriendid(friendid);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFriendid() {
        return friendid;
    }

    public void setFriendid(int friendid) {
        this.friendid = friendid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
