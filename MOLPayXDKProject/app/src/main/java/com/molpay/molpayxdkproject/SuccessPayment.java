package com.molpay.molpayxdkproject;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SuccessPayment extends AppCompatActivity {

    private Button btnExit;
    private TextView amountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_payment);
        Log.d("Transaction", "payment completed");
        successGif();

        Intent intent = getIntent();
        String amount = intent.getStringExtra("amount");

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(view -> btnExitOnClick());

        amountTV = findViewById(R.id.amountPaid);
        amountTV.setText("RM " + amount);

    }

    public void btnExitOnClick(){
        Intent intent = new Intent(SuccessPayment.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void successGif(){
        ImageView gifImageView = findViewById(R.id.successGif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.success)
                .into(gifImageView);
    }
}
