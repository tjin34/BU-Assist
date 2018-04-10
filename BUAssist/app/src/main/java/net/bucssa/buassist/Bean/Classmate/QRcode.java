package net.bucssa.buassist.Bean.Classmate;

/**
 * Created by Shinji on 2018/4/10.
 */

public class QRcode {
    private int meetingId;
    private long dateline;

    public QRcode(int meetingId, long dateline) {
        setDateline(dateline);
        setMeetingId(meetingId);
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public long getDateline() {
        return dateline;
    }

    public int getMeetingId() {
        return meetingId;
    }
}
