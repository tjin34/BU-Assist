package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class EditDoBReq {

    /**
     * uid : 0
     * birthyear : 1900
     * birthmonth : 1
     * birthday : 1
     * token :
     */

    private int uid;
    private int birthyear;
    private int birthmonth;
    private int birthday;
    private String token;

    public EditDoBReq(int uid, int birthyear, int birthmonth, int birthday, String token) {
        setToken(token);
        setUid(uid);
        setBirthday(birthday);
        setBirthmonth(birthmonth);
        setBirthyear(birthyear);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
