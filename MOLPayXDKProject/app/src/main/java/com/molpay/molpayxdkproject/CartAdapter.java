package com.molpay.molpayxdkproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<ItemModel> filteredItems;
    private final OnItemChangeListener onItemChangeListener;
    private SharedPreferenceManager sharedPreferenceManager;

    public CartAdapter(Context context, ArrayList<ItemModel> items, OnItemChangeListener onItemChangeListener) {
        this.context = context;
        this.filteredItems = filterItems(items);
        this.onItemChangeListener = onItemChangeListener;
        this.sharedPreferenceManager = new SharedPreferenceManager(context);
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        convertView.setVisibility(View.VISIBLE);

        ImageView itemPic = convertView.findViewById(R.id.itemPic);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemPrice = convertView.findViewById(R.id.itemPrice);
        TextView itemCounter = convertView.findViewById(R.id.itemCounter);
        Button itemPlus = convertView.findViewById(R.id.itemPlus);
        Button itemMinus = convertView.findViewById(R.id.itemMinus);

        ItemModel item = filteredItems.get(position);

        itemName.setText(item.getItem_name());
        itemPrice.setText(String.format("RM %.2f", item.getItem_price()));
        itemCounter.setText(String.valueOf(item.getCounter()));

        // Load image
        String imageName = item.getItem_name().toLowerCase().replace(" ", "_");
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        itemPic.setImageResource(imageResId);

        itemPlus.setOnClickListener(v -> {
            item.setCounter(item.getCounter() + 1);
            itemCounter.setText(String.valueOf(item.getCounter()));
            onItemChangeListener.onItemChanged();
            sharedPreferenceManager.saveItemList(filteredItems);
        });

        itemMinus.setOnClickListener(v -> {
            if (item.getCounter() > 0) {
                item.setCounter(item.getCounter() - 1);
                itemCounter.setText(String.valueOf(item.getCounter()));
                onItemChangeListener.onItemChanged();
                sharedPreferenceManager.saveItemList(filteredItems);
            }
        });

        return convertView;
    }

    private ArrayList<ItemModel> filterItems(ArrayList<ItemModel> allItems) {
        ArrayList<ItemModel> filteredList = new ArrayList<>();
        for (ItemModel item : allItems) {
            if (item.getCounter() > 0) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    public interface OnItemChangeListener {
        void onItemChanged();
    }

}
