package com.webandappdevelopment.skyler.modelclass;

public class CartList {
    private int id;
    private String title;
    private String price;
    private String img_url;
    private String details;

    public CartList(int id, String title, String price, String img_url, String details) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.img_url = img_url;
        this.details = details;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
}
