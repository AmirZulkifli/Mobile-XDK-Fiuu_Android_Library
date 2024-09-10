package com.molpay.molpayxdkproject;

public class ItemModel {

    // string course_name for storing course_name
    // and imgid for storing image id.
    private String item_name;
    private int imgid;
    private int counter;
    private double item_price;

    public ItemModel(String item_name, int imgid, double item_price) {
        this.item_name = item_name;
        this.imgid = imgid;
        this.counter = 0;
        this.item_price = item_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getItem_price() {
        return item_price;
    }

    public double setItem_price(Double item_price) {
        this.item_price = item_price;
        return item_price;
    }
}
