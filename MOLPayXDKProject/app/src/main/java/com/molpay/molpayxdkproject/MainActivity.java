package com.molpay.molpayxdkproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.wallet.button.PayButton;
import com.molpay.molpayxdk.MOLPayActivity;
import com.molpay.molpayxdk.googlepay.ActivityGP;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    private PayButton googlePayButton;

    private void restartmolpay() {
        HashMap<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put(MOLPayActivity.mp_amount, "1.01");

        // TODO: Enter your merchant account credentials before test run
        paymentDetails.put(MOLPayActivity.mp_username, "");
        paymentDetails.put(MOLPayActivity.mp_password, "");
        paymentDetails.put(MOLPayActivity.mp_merchant_ID, "");
        paymentDetails.put(MOLPayActivity.mp_app_name, "");
        paymentDetails.put(MOLPayActivity.mp_verification_key, "");

        paymentDetails.put(MOLPayActivity.mp_order_ID, Calendar.getInstance().getTimeInMillis());
        paymentDetails.put(MOLPayActivity.mp_currency, "MYR");
        paymentDetails.put(MOLPayActivity.mp_country, "MY");
        paymentDetails.put(MOLPayActivity.mp_channel, "multi");
        paymentDetails.put(MOLPayActivity.mp_bill_description, "bill description");
        paymentDetails.put(MOLPayActivity.mp_bill_name, "bill name");
        paymentDetails.put(MOLPayActivity.mp_bill_email, "example@gmail.com");
        paymentDetails.put(MOLPayActivity.mp_bill_mobile, "123456789");

        // TODO: Learn more about optional parameters here https://github.com/RazerMS/Mobile-XDK-RazerMS_Android_Studio/wiki/Installation-Guidance#prepare-the-payment-detail-object
//        paymentDetails.put(MOLPayActivity.mp_extended_vcode, false); // For Google Pay Only - Set true if your account enabled extended Verify Payment
//        paymentDetails.put(MOLPayActivity.mp_channel_editing, false);
//        paymentDetails.put(MOLPayActivity.mp_editing_enabled, true);
//        paymentDetails.put(MOLPayActivity.mp_express_mode, false);
//        paymentDetails.put(MOLPayActivity.mp_dev_mode, false);
//        paymentDetails.put(MOLPayActivity.mp_preferred_token, "new");

        Intent intent = new Intent(MainActivity.this, MOLPayActivity.class);
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        startActivityForResult(intent, MOLPayActivity.MOLPayXDK);
    }

    private void googlePayPayment() {
        HashMap<String, Object> paymentDetails = new HashMap<>();

        /*
            TODO: Follow Google’s instructions to request production access for your app: https://developers.google.com/pay/api/android/guides/test-and-deploy/request-prod-access
            *
             Choose the integration type Gateway when prompted, and provide screenshots of your app for review.
             After your app has been approved, test your integration in production by set mp_sandbox_mode = false & use production mp_verification_key & mp_merchant_ID.
             Then launching Google Pay from a signed, release build of your app.
             */
        paymentDetails.put(MOLPayActivity.mp_sandbox_mode, true); // Only set to false once you have request production access for your app

        // TODO: Enter your merchant account credentials before test run
        paymentDetails.put(MOLPayActivity.mp_merchant_ID, "SB_molpayxdk"); // Your sandbox / production merchant ID
        paymentDetails.put(MOLPayActivity.mp_verification_key, "4445db44bdb60687a8e7f7903a59c3a9");

        paymentDetails.put(MOLPayActivity.mp_amount, "1.01"); // Must be in 2 decimal points format
        paymentDetails.put(MOLPayActivity.mp_order_ID, Calendar.getInstance().getTimeInMillis()); // Must be unique
        paymentDetails.put(MOLPayActivity.mp_currency, "MYR"); // Must matched mp_country
        paymentDetails.put(MOLPayActivity.mp_country, "MY"); // Must matched mp_currency
        paymentDetails.put(MOLPayActivity.mp_bill_description, "The bill description");
        paymentDetails.put(MOLPayActivity.mp_bill_name, "The bill name");
        paymentDetails.put(MOLPayActivity.mp_bill_email, "payer.email@fiuu.com");
        paymentDetails.put(MOLPayActivity.mp_bill_mobile, "123456789");

        paymentDetails.put(MOLPayActivity.mp_extended_vcode, false); // Optional : Set true if your account enabled extended Verify Payment

        Intent intent = new Intent(MainActivity.this, ActivityGP.class); // Used ActivityGP for Google Pay
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        startActivityForResult(intent, MOLPayActivity.MOLPayXDK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("logGooglePay" , "onActivityResult requestCode = " + requestCode);
        Log.e("logGooglePay" , "onActivityResult resultCode = " + resultCode);

        if (requestCode == MOLPayActivity.MOLPayXDK && data != null){
            if (data.getStringExtra(MOLPayActivity.MOLPayTransactionResult) != null) {
                Log.d(MOLPayActivity.MOLPAY, "MOLPay result = " + data.getStringExtra(MOLPayActivity.MOLPayTransactionResult));
                TextView tw = findViewById(R.id.resultTV);
                tw.setText(data.getStringExtra(MOLPayActivity.MOLPayTransactionResult));
            }
        } else if (requestCode == MOLPayActivity.MOLPayXDK && resultCode == MainActivity.RESULT_CANCELED && data == null) {
            Log.e("logGooglePay" , "RESULT_CANCELED data == null");
            TextView tw = findViewById(R.id.resultTV);
            tw.setText("result = null");
        }

    }

    GridView itemGV;
    TextView itemCounter;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemGV = findViewById(R.id.gridView);
        itemCounter = findViewById(R.id.itemCounter);

        ArrayList<ItemModel> selectedItems = new ArrayList<>();

        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
        itemModelArrayList.add(new ItemModel("Burger", R.drawable.burger, 5.00));
        itemModelArrayList.add(new ItemModel("Chicken", R.drawable.chicken, 8.00));
        itemModelArrayList.add(new ItemModel("Fries", R.drawable.fries, 6.00));
        itemModelArrayList.add(new ItemModel("Pizza", R.drawable.pizza, 10.00));
        itemModelArrayList.add(new ItemModel("Nugget", R.drawable.nugget, 4.00));
        itemModelArrayList.add(new ItemModel("Porridge", R.drawable.porridge, 7.00));
        itemModelArrayList.add(new ItemModel("Satay", R.drawable.satay, 10.00));
        itemModelArrayList.add(new ItemModel("Wings", R.drawable.wings, 9.00));

        GVAdapter adapter = new GVAdapter(this, itemModelArrayList);
        itemGV.setAdapter(adapter);

        // Set an item click listener on the GridView
        itemGV.setOnItemClickListener((parent, view, position, id) -> {
            // Increment the counter on each click
            counter++;

            // Update the counter TextView
            itemCounter.setText("" + counter);

            ItemModel clickedItem = itemModelArrayList.get(position);
            clickedItem.setCounter(clickedItem.getCounter() + 1);

            if (!selectedItems.contains(clickedItem)) {
                selectedItems.add(clickedItem);
            }

        });

        //google pay button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //image button listener
        ImageButton cartButton = findViewById(R.id.cartButton);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an intent to navigate to CartActivity
                Intent intent = new Intent(MainActivity.this, CartActivity.class);

                // Create an ArrayList of item names and counters to pass
                ArrayList<String> selectedItemDetails = new ArrayList<>();
                for (ItemModel item : selectedItems) {
                    selectedItemDetails.add(item.getItem_name() + "-" + item.getCounter() + "-" + item.getItem_price());
                }

                // Pass the selected items as a string array list
                intent.putStringArrayListExtra("selectedItems", selectedItemDetails);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // START clicked
        if (id == R.id.newBtn) {
            restartmolpay();
        }

        return super.onOptionsItemSelected(item);
    }
}
