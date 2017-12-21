package net.bucssa.buassist.Bean.Classmate;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class Post {


    /**
     * classId : 4
     * postId : 12
     * authorId : 25
     * authorName : leeli123
     * subject : 123
     * content : 233
     * dateline : 1511896624
     * lastReply : 0
     * comment : 0
     * like : 0
     */

    private int classId;
    private int postId;
    private int authorId;
    private String authorName;
    private String subject;
    private String content;
    private int dateline;
    private int lastReply;
    private int comment;
    private int like;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public int getLastReply() {
        return lastReply;
    }

    public void setLastReply(int lastReply) {
        this.lastReply = lastReply;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
