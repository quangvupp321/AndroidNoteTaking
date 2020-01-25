package com.example.notetaking;

public class Note {
    private String header;
    private String content;
    private String tag;
    private String date;

    public Note(String header, String content, String tag, String date) {
        this.header = header;
        this.content = content;
        this.tag = tag;
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
