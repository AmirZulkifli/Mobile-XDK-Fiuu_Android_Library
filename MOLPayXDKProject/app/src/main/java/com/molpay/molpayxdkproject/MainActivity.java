package com.molpay.molpayxdkproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.molpay.molpayxdk.MOLPayActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    AppCompatEditText etAmount;
    AppCompatButton btnVTDeeplink;
    Intent intentDeeplink;

    AppCompatTextView txtPaymentStatus, txtFullResponse;

    private void restartmolpay(String strAmount) {
        HashMap<String, Object> paymentDetails = new HashMap<>();

        // --------------------------------- SKYBIZ_POS_TF -----------------------------------------

//        paymentDetails.put(MOLPayActivity.mp_amount, 2.00);
//        paymentDetails.put(MOLPayActivity.mp_username, "RMSxdk_2022");
//        paymentDetails.put(MOLPayActivity.mp_password, "RMSpwd@2022");
//        paymentDetails.put(MOLPayActivity.mp_merchant_ID, "rmsxdk_mobile_Dev");
//        paymentDetails.put(MOLPayActivity.mp_app_name, "mobile");
//        paymentDetails.put(MOLPayActivity.mp_verification_key, "ee738b541eff7b6b495e44771f71c0ec");
//        paymentDetails.put(MOLPayActivity.mp_order_ID, "order12345202371");
//        paymentDetails.put(MOLPayActivity.mp_currency, "MYR");
//        paymentDetails.put(MOLPayActivity.mp_country, "MY");
//        paymentDetails.put(MOLPayActivity.mp_channel, "multi");
//        paymentDetails.put(MOLPayActivity.mp_sandbox_mode, false);
//        paymentDetails.put(MOLPayActivity.mp_express_mode, false);
//        paymentDetails.put(MOLPayActivity.mp_bill_description, "description");
//        paymentDetails.put(MOLPayActivity.mp_bill_name, "molpay");
//        paymentDetails.put(MOLPayActivity.mp_bill_email, "example@mail.com");
//        paymentDetails.put(MOLPayActivity.mp_bill_mobile, "0113456789");
//        paymentDetails.put(MOLPayActivity.mp_dev_mode, false);
//        paymentDetails.put(MOLPayActivity.mp_editing_enabled, true);

//        paymentDetails.put(MOLPayActivity.mp_username,	"RMSxdk_2022");
//        paymentDetails.put(MOLPayActivity.mp_password,	"RMSpwd@2022");
//        paymentDetails.put(MOLPayActivity.mp_app_name,	"POSTF");
//        paymentDetails.put(MOLPayActivity.mp_verification_key,	"afef116d89aa1c55fb4ee5327e2f73e9");
//        paymentDetails.put(MOLPayActivity.mp_merchant_ID,	"STeatomartsdnbh");
//        paymentDetails.put(MOLPayActivity.mp_bill_name,	"SKYBIZ_POS_TF");
//        paymentDetails.put(MOLPayActivity.mp_amount,	2.00);
//        paymentDetails.put(MOLPayActivity.mp_bill_mobile,	"+60105000006");
//        paymentDetails.put(MOLPayActivity.mp_editing_enabled,	true);
//        paymentDetails.put(MOLPayActivity.mp_dev_mode,	false);
//        paymentDetails.put(MOLPayActivity.mp_order_ID,	Calendar.getInstance().getTimeInMillis());
//        paymentDetails.put(MOLPayActivity.mp_bill_description,	"Android POS tf");
//        paymentDetails.put(MOLPayActivity.mp_country,	"MY");
//        paymentDetails.put(MOLPayActivity.mp_express_mode,	false);
//        paymentDetails.put(MOLPayActivity.mp_channel,	"multi");
//        paymentDetails.put(MOLPayActivity.mp_bill_email,	"aminchai");
//        paymentDetails.put(MOLPayActivity.mp_preferred_token,	"new");
//        paymentDetails.put(MOLPayActivity.mp_channel_editing,	false);
//        paymentDetails.put(MOLPayActivity.mp_currency,	"MYR");

        // --------------------------------- DEEPLINK CIMB -----------------------------------------

//        paymentDetails.put(MOLPayActivity.mp_username, "RMSxdk_2022");
//        paymentDetails.put(MOLPayActivity.mp_password, "RMSpwd@2022");
//        paymentDetails.put(MOLPayActivity.mp_merchant_ID, "rmsxdk_mobile_Dev");
//        paymentDetails.put(MOLPayActivity.mp_app_name, "mobile");
//        paymentDetails.put(MOLPayActivity.mp_verification_key, "ee738b541eff7b6b495e44771f71c0ec");

        paymentDetails.put(MOLPayActivity.mp_amount, strAmount);

        paymentDetails.put(MOLPayActivity.mp_username, "api_SB_southerautocapital");
        paymentDetails.put(MOLPayActivity.mp_password, "UhO48Tv2LaNCqw");
        paymentDetails.put(MOLPayActivity.mp_merchant_ID, "SB_hafeez");
        paymentDetails.put(MOLPayActivity.mp_app_name, "app2app");
        paymentDetails.put(MOLPayActivity.mp_verification_key, "1bd95bd03061dca277aff58377de9399");

        paymentDetails.put(MOLPayActivity.mp_order_ID, Calendar.getInstance().getTimeInMillis());
        paymentDetails.put(MOLPayActivity.mp_currency, "MYR");
        paymentDetails.put(MOLPayActivity.mp_country, "MY");
        paymentDetails.put(MOLPayActivity.mp_bill_description, "Testing App to App");
        paymentDetails.put(MOLPayActivity.mp_bill_name, "App to App");
        paymentDetails.put(MOLPayActivity.mp_bill_email, "apptoapp@gmail.com");
        paymentDetails.put(MOLPayActivity.mp_bill_mobile, "01234567888");
        paymentDetails.put(MOLPayActivity.mp_channel, "multi");
//        paymentDetails.put(MOLPayActivity.mp_dev_mode, true);
        paymentDetails.put(MOLPayActivity.mp_dev_mode, false);

        //------------------------------------------------------------------------------------------

//        paymentDetails.put(MOLPayActivity.mp_channel_editing, true);
//        paymentDetails.put(MOLPayActivity.mp_editing_enabled, true);
//        paymentDetails.put(MOLPayActivity.mp_allowed_channels, "credit23");
//        paymentDetails.put(MOLPayActivity.mp_sandbox_mode, false);

//        paymentDetails.put(MOLPayActivity.mp_channel_editing, false);
//        paymentDetails.put(MOLPayActivity.mp_editing_enabled, true);
    //    paymentDetails.put(MOLPayActivity.mp_preferred_token, "new");
//        paymentDetails.put(MOLPayActivity.mp_express_mode, true);


        Intent intent = new Intent(MainActivity.this, MOLPayActivity.class);
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        startActivityForResult(intent, MOLPayActivity.MOLPayXDK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(MOLPayActivity.MOLPAY, "resultCode = "+resultCode);
        Log.d(MOLPayActivity.MOLPAY, "requestCode = "+requestCode);
        Log.d(MOLPayActivity.MOLPAY, "data = "+data.toString());

        if (requestCode == MOLPayActivity.MOLPayXDK && resultCode == RESULT_OK){
            Log.d(MOLPayActivity.MOLPAY, "MOLPay result = "+data.getStringExtra(MOLPayActivity.MOLPayTransactionResult));
            TextView tw = (TextView)findViewById(R.id.resultTV);
            tw.setText(data.getStringExtra(MOLPayActivity.MOLPayTransactionResult));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etAmount = findViewById(R.id.etAmount);
        btnVTDeeplink = findViewById(R.id.btnVTDeeplink);
        txtPaymentStatus = findViewById(R.id.txtPaymentStatus);
        txtFullResponse = findViewById(R.id.txtFullResponse);

        btnVTDeeplink.setOnClickListener(view -> {
            try {
                intentDeeplink = new Intent(Intent.ACTION_VIEW);
//                intentDeeplink.setData(Uri.parse("razervt://merchant.razer.com?merchantUrlScheme=merchantapp&merchantHost=m erchant.example.com&opType=SALE&currency=MYR&amount=1.10&orderId=ABC D1234&channel=CARD"));
                intentDeeplink.setData(Uri.parse("razervt://merchant.razer.com?merchantUrlScheme=merchantapp&merchantHost=merchant.example.com&opType=SALE&currency=MYR&amount=1.10&orderId=DT1707381972032&channel=CARD&payType=2"));
                startActivity(intentDeeplink);
            } catch (ActivityNotFoundException e) {
                // Define what your app should do if no activity can handle the intent.
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.rms.mobile.razervt")));
//                        e.printStackTrace();
            }
        });



        Log.e("logDeeplink" , "before get intent");
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if (data != null) {
            Log.e("logDeeplink" , "action = " + action);
            Log.e("logDeeplink" , "data = " + data);

            if (data.getQueryParameter("opType") != null) {
                if (data.getQueryParameter("opType").contains("SALE")){
                    txtPaymentStatus.setText("Lastest Payment Status : " + "Successfully paid RM" + data.getQueryParameter("amount") + " on " + data.getQueryParameter("payDate"));
                }
            }

            if (data.getQueryParameter("errorCode") != null) {
                if (data.getQueryParameter("errorCode").contains("9999")) {
                    txtPaymentStatus.setText("Lastest Payment Status : Payment has been cancelled");
                }
            }

            if (data.toString().contains("?Result=99&EndtoEndId=")) {
                int startIndexEndtoEndId = data.toString().indexOf("EndtoEndId=") + 11; // Get index of '[' and add 1 to start after it
                int startIndexEndtoEndIdSignature = data.toString().indexOf("EndtoEndIdSignature=") + 20; // Get index of '&' and add 1 to start after it
                int endIndexEndtoEndId = data.toString().indexOf('&', startIndexEndtoEndId); // Get index of '&' starting from startIndex
                String EndtoEndId = "";
                String EndtoEndIdSignature = "";

                if (startIndexEndtoEndId != 0 && endIndexEndtoEndId != -1) { // Ensure both characters are found
                    EndtoEndId = data.toString().substring(startIndexEndtoEndId, endIndexEndtoEndId);
                    Log.e("logDeeplink" , "EndtoEndId = " + EndtoEndId);
                }

                if (startIndexEndtoEndIdSignature != 0) {
                    EndtoEndIdSignature = data.toString().substring(startIndexEndtoEndIdSignature);
                    Log.e("logDeeplink" , "EndtoEndIdSignature = " + EndtoEndIdSignature);
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://uat3.cimbclicks.com.my/dobb2c/RPP/MY/Redirect/RTP?EndtoEndId=" + EndtoEndId + "&EndtoEndIdSignature=" + EndtoEndIdSignature + "&DbtrAgt=CIBBMYKL"));
                startActivity(browserIntent);
            } else {
                txtFullResponse.setText("Full url scheme response = " + data);
            }

        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // closebtn clicked
        if (id == R.id.newBtn) {

            if (Objects.requireNonNull(etAmount.getText()).toString().isEmpty()) {
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                restartmolpay(Objects.requireNonNull(etAmount.getText()).toString());
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
