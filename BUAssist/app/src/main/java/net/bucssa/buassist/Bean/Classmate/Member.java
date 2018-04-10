package net.bucssa.buassist.Bean.Classmate;

/**
 * Created by Shinji on 2017/12/21.
 */

public class Member {

    /**
     * userid : 59
     * username : NewSh1nJi
     * avatar : http://bucssa.net/uc_server/avatar.php?uid=59&size=middle
     * role : 3
     */

    private int userid;
    private String username;
    private String avatar;
    private int role;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
