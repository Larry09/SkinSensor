package com.example.lahirufernando.skinsensor;

/**
 * Created by lahirufernando on 30/11/2017.
 */
public class MetaData {
    int id;
    String time;
    String date;
    String Url;
    String comments;

    @Override
    public String toString() {
        return "MetaData{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", Url='" + Url + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    public MetaData(String time, String date, String url, String comments) {
        this.time = time;
        this.date = date;
        Url = url;
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public MetaData(int id, String time, String date, String url, String comments) {
        this.id = id;
        this.time = time;
        this.date = date;
        Url = url;
        this.comments = comments;
    }

    public MetaData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
