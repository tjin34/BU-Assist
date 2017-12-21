package net.bucssa.buassist.Bean.Classmate;

/**
 * Created by Shinji on 2017/12/21.
 */

public class Comment {


    /**
     * postId : 1
     * commentId : 1
     * content : 兄弟牛皮啊，这都让你加进来了
     * fromUid : 26
     * toUid : 0
     * dateline : 1504875708
     * fromUsername : Sh1nJi
     * avatar : http://bucssa.net/uc_server/avatar.php?uid=26&size=middle
     */

    private String postId;
    private String commentId;
    private String content;
    private String fromUid;
    private String toUid;
    private String dateline;
    private String fromUsername;
    private String avatar;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
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
