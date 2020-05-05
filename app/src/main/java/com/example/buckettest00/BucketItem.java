package com.example.buckettest00;

public class BucketItem {

    // 이후, 시간도 추가적으로 필요함
    private String id;
    private String title;
    private String detail;
    private String status;

    public BucketItem(){

    }

    public BucketItem(String title,String detail){
        this.title=title;
        this.detail=detail;
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
}
