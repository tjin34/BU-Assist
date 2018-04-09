package net.bucssa.buassist.Bean.Request;

/**
 * Created by Shinji on 2017/12/21.
 */

public class CreatePostReq {

    /**
     * uid : 0
     * classid : 0
     * subject :
     * content :
     * token :
     */

    private int uid;
    private int classid;
    private String subject;
    private String content;
    private String token;

    public CreatePostReq(int uid, int classid, String subject, String content, String token) {
        setClassid(classid);
        setUid(uid);
        setContent(content);
        setSubject(subject);
        setToken(token);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
