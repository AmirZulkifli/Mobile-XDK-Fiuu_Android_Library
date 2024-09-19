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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("logGooglePay" , "onActivityResult requestCode = " + requestCode);
        Log.e("logGooglePay" , "onActivityResult resultCode = " + resultCode);

        if (requestCode == MOLPayActivity.MOLPayXDK && data != null){
            if (data.getStringExtra(MOLPayActivity.MOLPayTransactionResult) != null) {
                Log.d(MOLPayActivity.MOLPAY, "MOLPay result = " + data.getStringExtra(MOLPayActivity.MOLPayTransactionResult));

                Intent intent = new Intent(MainActivity.this, SuccessPayment.class);
                startActivity(intent);
            }
        } else if (requestCode == MOLPayActivity.MOLPayXDK && resultCode == MainActivity.RESULT_CANCELED && data == null) {
            Log.e("logGooglePay" , "RESULT_CANCELED data == null");

            Intent intent = new Intent(MainActivity.this, FailPayment.class);
            startActivity(intent);
        }

    }

    GridView itemGV;
    TextView itemCounter;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemGV = findViewById(R.id.gridView);
        itemCounter = findViewById(R.id.itemCounter);
        counter = 0;

        ArrayList<ItemModel> selectedItems = new ArrayList<>();

        ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
        itemModelArrayList.add(new ItemModel("Burger", R.drawable.burger, 5.00));
        itemModelArrayList.add(new ItemModel("Chicken", R.drawable.chicken, 8.00));
        itemModelArrayList.add(new ItemModel("Fries", R.drawable.fries, 6.00));
        itemModelArrayList.add(new ItemModel("Pizza", R.drawable.pizza, 10.00));
        itemModelArrayList.add(new ItemModel("Nugget", R.drawable.nugget, 2.00));
        itemModelArrayList.add(new ItemModel("Porridge", R.drawable.porridge, 7.00));
        itemModelArrayList.add(new ItemModel("Satay", R.drawable.satay, 10.00));
        itemModelArrayList.add(new ItemModel("Wings", R.drawable.wings, 9.00));

        GVAdapter adapter = new GVAdapter(this, itemModelArrayList);
        itemGV.setAdapter(adapter);

        itemGV.setOnItemClickListener((parent, view, position, id) -> {
            counter++;
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

                if(counter == 0 ){

                    Toast.makeText(MainActivity.this,"Your cart is empty", Toast.LENGTH_SHORT).show();

                }else {

                    Intent intent = new Intent(MainActivity.this, CartActivity.class);

                    ArrayList<String> selectedItemDetails = new ArrayList<>();
                    for (ItemModel item : selectedItems) {
                        selectedItemDetails.add(item.getItem_name() + "-" + item.getCounter() + "-" + item.getItem_price());
                    }

                    intent.putStringArrayListExtra("selectedItems", selectedItemDetails);

                    startActivity(intent);
                }
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
