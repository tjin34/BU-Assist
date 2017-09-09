package net.bucssa.buassist.Bean.Message;

import java.io.Serializable;

/**
 * Created by KimuraShin on 17/6/22.
 */

public class Chat implements Serializable{


    /**
     * uid : 26
     * plid : 6
     * pmtype : 1
     * pmnum : 29
     * members : 2
     * subject : 2017-7-14 消息接口测试
     * summary : leeli123 : Hello WORLDDDD~~~
     * touid : 25
     * tousername : leeli123
     * avatar : http://bucssa.net/uc_server/avatar.php?uid=25&size=middle
     * daterange : 5
     * createdateline : 1499933595
     * lastdateline : 1500865626
     * hasnew : 1
     */

    private int uid;
    private int plid;
    private int pmtype;
    private int pmnum;
    private int members;
    private String subject;
    private String summary;
    private int touid;
    private String tousername;
    private String avatar;
    private int daterange;
    private String createdateline;
    private String lastdateline;
    private int hasnew;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }

    public int getPmtype() {
        return pmtype;
    }

    public void setPmtype(int pmtype) {
        this.pmtype = pmtype;
    }

    public int getPmnum() {
        return pmnum;
    }

    public void setPmnum(int pmnum) {
        this.pmnum = pmnum;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getTouid() {
        return touid;
    }

    public void setTouid(int touid) {
        this.touid = touid;
    }

    public String getTousername() {
        return tousername;
    }

    public void setTousername(String tousername) {
        this.tousername = tousername;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getDaterange() {
        return daterange;
    }

    public void setDaterange(int daterange) {
        this.daterange = daterange;
    }

    public String getCreatedateline() {
        return createdateline;
    }

    public void setCreatedateline(String createdateline) {
        this.createdateline = createdateline;
    }

    public String getLastdateline() {
        return lastdateline;
    }

    public void setLastdateline(String lastdateline) {
        this.lastdateline = lastdateline;
    }

    public int getHasnew() {
        return hasnew;
    }

    public void setHasnew(int hasnew) {
        this.hasnew = hasnew;
    }
}
