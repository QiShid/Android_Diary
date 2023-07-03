package com.example.diary;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class Diary {
    private int id = 0;
    private String title = "";
    private String content = "";
    private String time;
    private String author = "";

    public Diary(){
        this.time = getCurrentTime();
    }

    public Diary(String title,String conten,String author){
        this.title = title;
        this.content = conten;
        this.time = getCurrentTime();
        this.author = author;
    }

    public Diary(String title,String conten,String time,String author,int id){
        this.title = title;
        this.content = conten;
        this.time = time;
        this.author = author;
        this.id = id;
    }

    private String getCurrentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
