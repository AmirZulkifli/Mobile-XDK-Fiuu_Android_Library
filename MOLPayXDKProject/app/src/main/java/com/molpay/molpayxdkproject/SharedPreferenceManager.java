package com.molpay.molpayxdkproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferenceManager {

    private static final String PREF_NAME = "CartData";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;


    public SharedPreferenceManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void saveItemList(ArrayList<ItemModel> items){
        String json = gson.toJson(items);
        editor.putString("cart_items",json);
        editor.apply();
        Log.d("Main", "json " + json);
    }

    public ArrayList<ItemModel> getItemList(){
        String json = sharedPreferences.getString("cart_items",null);
        Type type  =new TypeToken<ArrayList<ItemModel>>() {}.getType();
        return gson.fromJson(json,type);
    }

    public void clearCart(){
     editor.clear();
     editor.apply();
    }
}
