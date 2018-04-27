package net.bucssa.buassist.Bean.Request;

import net.bucssa.buassist.UserSingleton;

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
    private String memberuid;
    private int groupid;
    private String token;

    public GroupInviteReq(int uid, String memberuid,int groupid, String token) {
        setUid(uid);
        setMemberuid(memberuid);
        setGroupid(groupid);
        setToken(token);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMemberuid() {
        return memberuid;
    }

    public void setMemberuid(String memberuid) {
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
