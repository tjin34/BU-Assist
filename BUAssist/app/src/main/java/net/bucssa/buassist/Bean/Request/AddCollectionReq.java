package net.bucssa.buassist.Bean.Request;

/**
 * Created by KimuraShin on 17/8/21.
 */

public class AddCollectionReq {


    /**
     * uid : 0
     * tid : 0
     * subject :
     * author :
     * dateline : 0
     * token :
     */

    private int uid;
    private int tid;
    private String subject;
    private String author;
    private int dateline;
    private String token;

    public AddCollectionReq(int uid, int tid, String subject, String author, int dateline, String token) {
        setUid(uid);
        setAuthor(author);
        setDateline(dateline);
        setSubject(subject);
        setTid(tid);
        setToken(token);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
