package net.bucssa.buassist.Bean.Message;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class Message {


    /**
     * plid : 6
     * authorid : 25
     * pmtype : 1
     * subject : 2017-7-14 消息接口测试
     * members : 2
     * dateline : 1500865258
     * pmid : 117
     * message : Message
     * founderuid : 26
     * founddateline : 1499933595
     * touid : 26
     * author : leeli123
     * authoravatar : http://bucssa.net/uc_server/avatar.php?uid=25&size=small
     * msgfromid : 25
     * msgfrom : leeli123
     * msgtoid : 26
     */

    private int plid;
    private int authorid;
    private int pmtype;
    private String subject;
    private int members;
    private int dateline;
    private int pmid;
    private String message;
    private int founderuid;
    private String founddateline;
    private int touid;
    private String author;
    private String authoravatar;
    private int msgfromid;
    private String msgfrom;
    private int msgtoid;

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public int getPmtype() {
        return pmtype;
    }

    public void setPmtype(int pmtype) {
        this.pmtype = pmtype;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public int getPmid() {
        return pmid;
    }

    public void setPmid(int pmid) {
        this.pmid = pmid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFounderuid() {
        return founderuid;
    }

    public void setFounderuid(int founderuid) {
        this.founderuid = founderuid;
    }

    public String getFounddateline() {
        return founddateline;
    }

    public void setFounddateline(String founddateline) {
        this.founddateline = founddateline;
    }

    public int getTouid() {
        return touid;
    }

    public void setTouid(int touid) {
        this.touid = touid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthoravatar() {
        return authoravatar;
    }

    public void setAuthoravatar(String authoravatar) {
        this.authoravatar = authoravatar;
    }

    public int getMsgfromid() {
        return msgfromid;
    }

    public void setMsgfromid(int msgfromid) {
        this.msgfromid = msgfromid;
    }

    public String getMsgfrom() {
        return msgfrom;
    }

    public void setMsgfrom(String msgfrom) {
        this.msgfrom = msgfrom;
    }

    public int getMsgtoid() {
        return msgtoid;
    }

    public void setMsgtoid(int msgtoid) {
        this.msgtoid = msgtoid;
    }
}
