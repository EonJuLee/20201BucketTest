package com.example.buckettest00;

import java.io.Serializable;

public class DiaryItem implements Serializable {
    private String id;
    private String title;
    private String detail;
    private String image;

    public DiaryItem(){

    }

    public DiaryItem(String title, String detail, String image) {
        this.title = title;
        this.detail = detail;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
