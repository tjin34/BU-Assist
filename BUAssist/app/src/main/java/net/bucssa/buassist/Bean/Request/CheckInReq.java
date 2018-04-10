package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2018/4/10.
 */

public class CheckInReq {

    /**
     * uid : 0
     * meetingId : 0
     * token :
     */

    private int uid;
    private int meetingId;
    private String token;

    public CheckInReq(int uid, int meetingId, String token) {
        setMeetingId(meetingId);
        setToken(token);
        setUid(uid);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
