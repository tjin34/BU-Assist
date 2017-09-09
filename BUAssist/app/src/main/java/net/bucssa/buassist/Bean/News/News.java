package net.bucssa.buassist.Bean.News;

/**
 * Created by Shin on 17/6/22
 */

public class News {
    private int ID;
    private String Title;
    private String Content;
    private String Reportor;
    private String ReportTime;
    private String newsURL;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setReportor(String reportor) {
        Reportor = reportor;
    }

    public void setReportTime(String reportTime) {
        ReportTime = reportTime;
    }

    public void setNewsURL(String newsURL) {
        this.newsURL = newsURL;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public String getReportor() {
        return Reportor;
    }

    public String getReportTime() {
        return ReportTime;
    }

    public String getNewsURL() {
        return newsURL;
    }
}

