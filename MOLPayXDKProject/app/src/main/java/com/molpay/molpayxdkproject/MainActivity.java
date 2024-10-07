package com.molpay.molpayxdkproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.molpay.molpayxdk.MOLPayActivity;

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
        molPayLauncher.launch(intent);
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
    private GVAdapter adapter;
    private SharedPreferenceManager preferenceManager;
    private ArrayList<ItemModel> itemModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared preference test
        preferenceManager = new SharedPreferenceManager(this);

        itemGV = findViewById(R.id.gridView);
        itemCounter = findViewById(R.id.itemCounter);

        ArrayList<ItemModel> selectedItems = new ArrayList<>();

        Log.d("Main", "shared " +preferenceManager.getItemList());

        if (preferenceManager.getItemList() == null){
            itemModelArrayList = new ArrayList<>();
            itemModelArrayList.add(new ItemModel("Burger", R.drawable.burger, 5.00));
            itemModelArrayList.add(new ItemModel("Chicken", R.drawable.chicken, 8.00));
            itemModelArrayList.add(new ItemModel("Fries", R.drawable.fries, 6.00));
            itemModelArrayList.add(new ItemModel("Pizza", R.drawable.pizza, 10.00));
            itemModelArrayList.add(new ItemModel("Nugget", R.drawable.nugget, 2.00));
            itemModelArrayList.add(new ItemModel("Porridge", R.drawable.porridge, 7.00));
            itemModelArrayList.add(new ItemModel("Satay", R.drawable.satay, 10.00));
            itemModelArrayList.add(new ItemModel("Wings", R.drawable.wings, 9.00));
        }else{
            itemModelArrayList = preferenceManager.getItemList();
        }

        adapter = new GVAdapter(this, itemModelArrayList);
        itemGV.setAdapter(adapter);

        itemGV.setOnItemClickListener((parent, view, position, id) -> {
            counter++;
            itemCounter.setText(String.valueOf(counter));

            ItemModel clickedItem = itemModelArrayList.get(position);
            clickedItem.setCounter(clickedItem.getCounter() + 1);

            if (!selectedItems.contains(clickedItem)) {
                selectedItems.add(clickedItem);
            }
            adapter.notifyDataSetChanged();
            preferenceManager.saveItemList(itemModelArrayList);
        });

        //google pay button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //image button listener
        ImageButton cartButton = findViewById(R.id.cartButton);

        cartButton.setOnClickListener(v -> {
            if(counter == 0 ){
                Toast.makeText(MainActivity.this,"Your cart is empty", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (preferenceManager.getItemList() != null){
            ArrayList<ItemModel> updatedList = preferenceManager.getItemList();
            adapter.clear();
            adapter.addAll(updatedList);
            adapter.notifyDataSetChanged();

            counter = getTotalItemCount(updatedList);

            itemCounter.setText(String.valueOf(counter));
        }
    }

    private int getTotalItemCount(ArrayList<ItemModel> itemList){
        int totalCount = 0;

        for(ItemModel item: itemList){
            totalCount += item.getCounter();
        }

        return totalCount;
    }

    //ActivityResultLauncher replacing deprecated startActivityResult

    ActivityResultLauncher<Intent> molPayLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String molpayResult = data.getStringExtra(MOLPayActivity.MOLPayTransactionResult);
                        Log.d("MOLPay", "MOLPay result: " + molpayResult);
                    }
                }
            }
    );

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

        if (id == R.id.infoBtn){

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog,null);

            TextView title = dialogView.findViewById(R.id.dialogTitle);
            TextView message = dialogView.findViewById(R.id.dialogMessage);
            Button exit = dialogView.findViewById(R.id.btnOK);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setCancelable(true)
                    .create();
            exit.setOnClickListener(v ->{
                dialog.dismiss();
            });

            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
