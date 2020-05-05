package com.example.buckettest00;

public class CateItem {
    private String id;
    private String tag;

    public CateItem(){

    }

    public CateItem(String tag){
        this.tag=tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
