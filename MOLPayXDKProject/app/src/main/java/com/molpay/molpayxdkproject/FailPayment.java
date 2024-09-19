package com.molpay.molpayxdkproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FailPayment extends AppCompatActivity {

    private Button btnBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fail_payment);

        Log.d("Transaction", "payment failed");

        Toast.makeText(this,"Insufficient fund", Toast.LENGTH_LONG).show();
        failGif();
    }

    public void failGif(){
        ImageView gifImageView = findViewById(R.id.successGif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.error)
                .into(gifImageView);
    }

}
