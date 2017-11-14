package net.bucssa.buassist.Bean.Message;

/**
 * Created by tjin3 on 2017/11/12.
 */

public class SystemNotification {

    /**
     * id : 3
     * uid : 26
     * category : 2
     * type : 好友
     * is_new : 1
     * authorid : 64
     * author :
     * content : mucunxiner希望与你成为好友！
     * dateline : 1509608471
     * from_id : 64
     * from_type : 好友请求
     * delstatus : 0
     */

    private int id;
    private int uid;
    private int category;
    private String type;
    private int is_new;
    private int authorid;
    private String author;
    private String content;
    private int dateline;
    private int from_id;
    private String from_type;
    private int delstatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public String getFrom_type() {
        return from_type;
    }

    public void setFrom_type(String from_type) {
        this.from_type = from_type;
    }

    public int getDelstatus() {
        return delstatus;
    }

    public void setDelstatus(int delstatus) {
        this.delstatus = delstatus;
    }
}