package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2017/12/21.
 */

public class CreateGroupReq {

    /**
     * uid : 0
     * groupName :
     * groupIntro :
     * tags :
     * token :
     */

    private int uid;
    private String groupName;
    private String groupIntro;
    private String tags;
    private String token;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupIntro() {
        return groupIntro;
    }

    public void setGroupIntro(String groupIntro) {
        this.groupIntro = groupIntro;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
