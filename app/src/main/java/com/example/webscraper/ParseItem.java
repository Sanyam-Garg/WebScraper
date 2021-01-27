package com.example.webscraper;

public class ParseItem {
    private String imgUrl, title;

    public ParseItem(){

    }

    public ParseItem(String imgUrl, String title){
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public String getImgUrl(){
        return imgUrl;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }


}
