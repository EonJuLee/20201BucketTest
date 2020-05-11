package com.example.buckettest00;

public class BucketItem {

    // 이후, 시간도 추가적으로 필요함
    private String id;
    private String title;
    private String detail;
    private String status;
    private String cgory;
    private String hash1,hash2;

    public BucketItem(){

    }

    // 데베에 넣기 위해 꼭 필요한 것만 넣음
    public BucketItem(String title, String detail, String cgory){
        this.title=title;
        this.detail=detail;
        this.cgory = cgory;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCgory() {
        return cgory;
    }

    public void setCgory(String cgory) {
        this.cgory = cgory;
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
}
