package net.bucssa.buassist.Bean.Temp;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class Post {
    private String type;
    private String title;
    private String time;
    /**
     * postClass : CS 101 A1
     * isHot : 1
     * author : 作者
     * content : 内容
     */

    private String postClass;
    private int isHot;
    private String author;
    private String content;

    public Post(String title, String postClass, String author, String content, String time, int isHot){
        this.title = title;
        this.postClass = postClass;
        this.author = author;
        this.content = content;
        this.time = time;
        this.isHot = isHot;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getPostClass() {
        return postClass;
    }

    public void setPostClass(String postClass) {
        this.postClass = postClass;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
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
}
