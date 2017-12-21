package net.bucssa.buassist.Bean.Login;

/**
 * Created by Shinji on 2017/12/21.
 */

public class OtherInfo {

    /**
     * uid : 24
     * nickname : cssa
     * realname : cssa
     * gender : 0
     * dateOfBirth : 2017-7-4
     * bio :
     * affectivestatus :
     * college : College of Arts and Sciences (CAS)
     * major : Undeclared
     * avatar : http://bucssa.net/uc_server/avatar.php?uid=24&size=big
     * isFriend : true
     * plid : 3
     */

    private int uid;
    private String nickname;
    private String realname;
    private int gender;
    private String dateOfBirth;
    private String bio;
    private String affectivestatus;
    private String college;
    private String major;
    private String avatar;
    private boolean isFriend;
    private int plid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAffectivestatus() {
        return affectivestatus;
    }

    public void setAffectivestatus(String affectivestatus) {
        this.affectivestatus = affectivestatus;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIsFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }
}
