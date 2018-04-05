package net.bucssa.buassist.Bean.Classmate;

import java.io.Serializable;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class Group implements Serializable{


    /**
     * groupId : 10012
     * groupName : 私の世界
     * groupIntro : 一緒に限界を超えて！
     * groupTag : CS 101
     * creatorId : 26
     * creatorName : Sh1nJi
     * members : 2
     * credit : 0
     * plid : 0
     */

    private int groupId;
    private String groupName;
    private String groupIntro;
    private String groupTag;
    private int creatorId;
    private String creatorName;
    private int members;
    private int credit;
    private int plid;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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

    public String getGroupTag() {
        return groupTag;
    }

    public void setGroupTag(String groupTag) {
        this.groupTag = groupTag;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }
}
