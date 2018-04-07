package net.bucssa.buassist.Bean.Classmate;

/**
 * Created by Shinji on 2017/12/21.
 */

public class Comment {


    /**
     * postId : 3
     * commentId : 11
     * content : 23333
     * fromUid : 25
     * toUid : 0
     * dateline : 1511738849
     * fromUsername : leeli123
     * avatar : http://bucssa.net/uc_server/avatar.php?uid=25&size=middle
     */

    private int postId;
    private int commentId;
    private String content;
    private int fromUid;
    private int toUid;
    private int dateline;
    private String fromUsername;
    private String avatar;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFromUid() {
        return fromUid;
    }

    public void setFromUid(int fromUid) {
        this.fromUid = fromUid;
    }

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public int getDateline() {
        return dateline;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
