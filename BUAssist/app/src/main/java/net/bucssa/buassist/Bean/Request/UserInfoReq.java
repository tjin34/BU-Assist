package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/7/10.
 */

public class UserInfoReq {


    /**
     * uid : 26
     * realname : 金正源
     * gender : 1
     * birthyear : 1996
     * birthmonth : 1
     * birthday : 1
     */

    private int uid;
    private String realname;
    private int gender;
    private int birthyear;
    private int birthmonth;
    private int birthday;

    public UserInfoReq(int uid, String name, int sex, int year, int month, int day) {
        this.uid = uid;
        this.realname = name;
        this.gender = sex;
        this.birthyear = year;
        this.birthmonth = month;
        this.birthday = day;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public int getBirthmonth() {
        return birthmonth;
    }

    public void setBirthmonth(int birthmonth) {
        this.birthmonth = birthmonth;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }
}
