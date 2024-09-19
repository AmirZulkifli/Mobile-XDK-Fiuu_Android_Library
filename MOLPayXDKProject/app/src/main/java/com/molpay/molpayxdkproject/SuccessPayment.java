package com.molpay.molpayxdkproject;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SuccessPayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_payment);
        Log.d("Transaction", "payment completed");
        successGif();
    }

    public void successGif(){
        ImageView gifImageView = findViewById(R.id.successGif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.success)
                .into(gifImageView);
    }
}
