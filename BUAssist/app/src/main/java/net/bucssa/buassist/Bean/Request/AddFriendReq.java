package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class AddFriendReq {


    /**
     * uid : 0
     * friendid : 0
     * comment :
     * token :
     */

    private int uid;
    private int friendid;
    private String comment;
    private String token;

    public AddFriendReq(int uid, int friendid, String comment, String token) {
        setToken(token);
        setUid(uid);
        setFriendid(friendid);
        setComment(comment);
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
