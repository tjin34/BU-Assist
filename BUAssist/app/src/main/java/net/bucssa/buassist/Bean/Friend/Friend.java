package net.bucssa.buassist.Bean.Friend;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class Friend {


    /**
     * friendid : 24
     * username : cssa
     * comment : 还可以哈哈哈哈
     * avatar : http://bucssa.net/uc_server/avatar.php?uid=24&size=middle
     */

    private String friendid;
    private String username;
    private String comment;
    private String avatar;

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
