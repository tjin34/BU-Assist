package net.bucssa.buassist.Bean.Classmate;

/**
 * Created by Shinji on 2018/4/10.
 */

public class Meeting {


    /**
     * meetingId : 3
     * groupId : 10013
     * checked : 0
     * created : 1523386985
     * finished : 0
     * isHalf : 0
     */

    private int meetingId;
    private int groupId;
    private int checked;
    private int created;
    private int finished;
    private int isHalf;

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getIsHalf() {
        return isHalf;
    }

    public void setIsHalf(int isHalf) {
        this.isHalf = isHalf;
    }
}
