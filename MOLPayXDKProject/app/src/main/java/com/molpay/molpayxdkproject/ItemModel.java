package com.molpay.molpayxdkproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ItemModel implements Parcelable {

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

    // Parcelable implementation
    protected ItemModel(Parcel in) {
        item_name = in.readString();
        imgid = in.readInt();
        counter = in.readInt();
        item_price = in.readDouble();
    }

    public static ArrayList<ItemModel> itemList(){
        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
        itemModelArrayList.add(new ItemModel("Burger", R.drawable.burger, 5.00));
        itemModelArrayList.add(new ItemModel("Chicken", R.drawable.chicken, 8.00));
        itemModelArrayList.add(new ItemModel("Fries", R.drawable.fries, 6.00));
        itemModelArrayList.add(new ItemModel("Pizza", R.drawable.pizza, 10.00));
        itemModelArrayList.add(new ItemModel("Nugget", R.drawable.nugget, 2.00));
        itemModelArrayList.add(new ItemModel("Porridge", R.drawable.porridge, 7.00));
        itemModelArrayList.add(new ItemModel("Satay", R.drawable.satay, 10.00));
        itemModelArrayList.add(new ItemModel("Wings", R.drawable.wings, 9.00));
        return itemModelArrayList;
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item_name);
        dest.writeInt(imgid);
        dest.writeInt(counter);
        dest.writeDouble(item_price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters
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

    public void resetCounter(){
        this.counter = 0;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }
}
