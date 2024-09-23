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

        Toast.makeText(this,"Payment failed", Toast.LENGTH_LONG).show();
        failGif();
        btnBackToMenu = findViewById(R.id.btnMenu);
        btnBackToMenu.setOnClickListener(view -> btnMenuOnClick());
    }

    public void btnMenuOnClick(){
        Intent intent = new Intent(FailPayment.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void failGif(){
        ImageView gifImageView = findViewById(R.id.successGif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.error)
                .into(gifImageView);
    }

}
