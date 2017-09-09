package net.bucssa.buassist.Bean.News;

/**
 * Created by KimuraShin on 17/8/9.
 */

public class NewsContent {


    /**
     * tid : 1661
     * author : BUCSSA
     * authorid : 2
     * authorAvatar : getAvatar
     * subject : 【租房信息】波士顿租房经典问题：中介费是什么，到底要不要交？ ...
     * dateline : 1501257600
     * message : content
     * comment : 0
     */

    private int tid;
    private String author;
    private int authorid;
    private String authorAvatar;
    private String subject;
    private String dateline;
    private String message;
    private String comment;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
