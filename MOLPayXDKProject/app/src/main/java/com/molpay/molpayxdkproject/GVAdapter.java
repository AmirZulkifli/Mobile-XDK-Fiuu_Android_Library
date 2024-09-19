package com.molpay.molpayxdkproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class GVAdapter extends ArrayAdapter<ItemModel> {

    public GVAdapter(@NonNull Context context, ArrayList<ItemModel> itemModelArrayList) {
        super(context, 0, itemModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        ItemModel itemModel = getItem(position);
        TextView itemNameTV = listitemView.findViewById(R.id.itemNameTV);
        TextView itemPriceTV = listitemView.findViewById(R.id.itemPriceTV);
        ImageView itemIV = listitemView.findViewById(R.id.itemIV);

        itemNameTV.setText(itemModel.getItem_name());
        itemPriceTV.setText(String.format("RM %.2f", itemModel.getItem_price()));
        itemIV.setImageResource(itemModel.getImgid());
        return listitemView;
    }
}

