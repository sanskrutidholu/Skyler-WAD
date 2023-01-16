package com.webandappdevelopment.skyler.modelclass;

public class AddressList {
    private int id;
    private String pincode;
    private String state;
    private String city;
    private String house;
    private String area;
    private String tag;

    public AddressList(int id, String pincode, String state, String city, String house, String area,String tag) {
        this.id = id;
        this.pincode = pincode;
        this.state = state;
        this.city = city;
        this.house = house;
        this.area = area;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
