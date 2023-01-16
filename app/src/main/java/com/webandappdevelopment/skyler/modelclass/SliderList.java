package com.webandappdevelopment.skyler.modelclass;

public class SliderList {
    int img_id;
    String img_url;
//    String city;

    public SliderList(int img_id, String img_url) {
        this.img_id = img_id;
        this.img_url = img_url;
//        this.city = city;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
}
