package com.example.buckettest00;

import java.io.Serializable;

public class DataItem implements Serializable {
    private String title;
    private String hash1,hash2,hash3;
    private String detail;
    private String user;
    private String picture;

    public DataItem(){

    }

    public DataItem(String title, String hash1, String hash2, String hash3, String detail, String user) {
        this.title = title;
        this.hash1 = hash1;
        this.hash2 = hash2;
        this.hash3 = hash3;
        this.detail = detail;
        this.user = user;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHash1() {
        return hash1;
    }

    public void setHash1(String hash1) {
        this.hash1 = hash1;
    }

    public String getHash2() {
        return hash2;
    }

    public void setHash2(String hash2) {
        this.hash2 = hash2;
    }

    public String getHash3() {
        return hash3;
    }

    public void setHash3(String hash3) {
        this.hash3 = hash3;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
