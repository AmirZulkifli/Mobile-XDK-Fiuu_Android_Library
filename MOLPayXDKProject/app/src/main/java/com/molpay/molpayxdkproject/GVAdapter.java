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
        TextView itemTV = listitemView.findViewById(R.id.itemTV);
        ImageView itemIV = listitemView.findViewById(R.id.itemIV);

        itemTV.setText(itemModel.getItem_name());
        itemIV.setImageResource(itemModel.getImgid());
        return listitemView;
    }
}

