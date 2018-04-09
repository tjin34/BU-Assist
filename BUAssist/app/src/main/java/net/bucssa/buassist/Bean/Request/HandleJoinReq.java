package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2017/12/21.
 */

public class HandleJoinReq {

    /**
     * nid : 0
     * uid : 0
     * memberuid : 0
     * groupid : 0
     * result : 0
     * comment :
     * token :
     */

    private int nid;
    private int uid;
    private int memberuid;
    private int groupid;
    private int result;
    private String comment;
    private String token;

    public HandleJoinReq(int nid, int uid, int memberuid, int groupid, int result, String comment, String token){
        this.nid = nid;
        this.uid = uid;
        this.memberuid = memberuid;
        this.groupid = groupid;
        this.result = result;
        this.comment = comment;
        this.token = token;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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
