package com.molpay.molpayxdkproject;

import java.util.ArrayList;

public class ItemDataManager {
    private static ItemDataManager instance;
    private ArrayList<ItemModel> selectedItems;

    private ItemDataManager() {
        selectedItems = new ArrayList<>();
    }

    public static ItemDataManager getInstance() {
        if (instance == null) {
            instance = new ItemDataManager();
        }
        return instance;
    }

    public ArrayList<ItemModel> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(ArrayList<ItemModel> items) {
        selectedItems = items;
    }
}
