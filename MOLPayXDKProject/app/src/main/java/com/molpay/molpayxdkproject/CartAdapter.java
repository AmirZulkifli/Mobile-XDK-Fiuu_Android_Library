package com.molpay.molpayxdkproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.molpay.molpayxdkproject.ItemModel;
import com.molpay.molpayxdkproject.R;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<String> itemNames;
    private final ArrayList<Integer> itemCounters;
    private final ArrayList<Double> itemPrices;

    public CartAdapter(Context context, ArrayList<String> itemNames, ArrayList<Integer> itemCounters, ArrayList<Double> itemPrices) {
        this.context = context;
        this.itemNames = itemNames;
        this.itemCounters = itemCounters;
        this.itemPrices = itemPrices;
    }

    @Override
    public int getCount() {
        return itemNames.size();
    }

    @Override
    public Object getItem(int position) {
        return itemNames.get(position);
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

        ImageView itemPic = convertView.findViewById(R.id.itemPic);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemCounter = convertView.findViewById(R.id.itemCounter);

        // Set item details
        itemName.setText(itemNames.get(position));
        itemCounter.setText(String.valueOf(itemCounters.get(position)));

        // Set item image
        String imageName = itemNames.get(position).toLowerCase().replace(" ", "_");
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        itemPic.setImageResource(imageResId);

        return convertView;
    }
}
