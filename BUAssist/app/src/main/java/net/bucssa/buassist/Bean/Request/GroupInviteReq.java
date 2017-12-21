package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2017/12/21.
 */

public class GroupInviteReq {


    /**
     * uid : 0
     * memberuid : 0
     * groupid : 0
     * token :
     */

    private int uid;
    private int memberuid;
    private int groupid;
    private String token;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMemberuid() {
        return memberuid;
    }

    public void setMemberuid(int memberuid) {
        this.memberuid = memberuid;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
