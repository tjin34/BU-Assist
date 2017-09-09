package net.bucssa.buassist.Bean.Thread;

/**
 * Created by KimuraShin on 17/7/6.
 */

public class TuiSong {


    /**
     * tid : 1654
     * author : BUCSSA
     * authorid : 2
     * subject : 【新生见面会】北京站！2017 BUCSSA新生见面会信息&amp;指南（换地址啦~） ...
     * dateline : 1499788800
     * views : 190
     * replies : 0
     * coverpath : data/attachment/forum/threadcover/9d/26/1654.jpg
     * url : http://demo.bucssa.net/forum.php?mod=viewthread&tid=1654&extra=page=1&mobile=2
     * isCollected : true
     */

    private int tid;
    private String author;
    private int authorid;
    private String subject;
    private int dateline;
    private int views;
    private int replies;
    private String coverpath;
    private String url;
    private boolean isCollected;


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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public String getCoverpath() {
        return coverpath;
    }

    public void setCoverpath(String coverpath) {
        this.coverpath = coverpath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void getMonthAndDay() {

    }

    public boolean isIsCollected() {
        return isCollected;
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }
}
