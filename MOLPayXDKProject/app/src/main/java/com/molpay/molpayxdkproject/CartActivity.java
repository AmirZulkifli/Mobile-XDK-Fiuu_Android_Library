package com.molpay.molpayxdkproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.wallet.button.ButtonConstants;
import com.google.android.gms.wallet.button.ButtonOptions;
import com.google.android.gms.wallet.button.PayButton;
import com.molpay.molpayxdk.MOLPayActivity;
import com.molpay.molpayxdk.googlepay.ActivityGP;
import com.molpay.molpayxdk.googlepay.UtilGP;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemChangeListener{

    private PayButton googlePayButton;
    private GridView cartView;
    private TextView totalPriceTV;
    private ArrayList<ItemModel> itemList;
    private CartAdapter cartAdapter;

    private int totalItem = 0;
    private double totalPrice = 0.00;
    private String totalPriceText;


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

        Intent intent = new Intent(CartActivity.this, MOLPayActivity.class);
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        startActivityForResult(intent, MOLPayActivity.MOLPayXDK);
    }

    private void googlePayPayment() {
        Log.d("Cart", "Total price: " + totalPriceText);
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

        paymentDetails.put(MOLPayActivity.mp_amount, totalPriceText); // Must be in 2 decimal points format
        //paymentDetails.put(MOLPayActivity.mp_amount, "4.99"); // Must be in 2 decimal points format
        paymentDetails.put(MOLPayActivity.mp_order_ID, Calendar.getInstance().getTimeInMillis()); // Must be unique
        paymentDetails.put(MOLPayActivity.mp_currency, "MYR"); // Must matched mp_country
        paymentDetails.put(MOLPayActivity.mp_country, "MY"); // Must matched mp_currency
        paymentDetails.put(MOLPayActivity.mp_bill_description, "The bill description");
        paymentDetails.put(MOLPayActivity.mp_bill_name, "The bill name");
        paymentDetails.put(MOLPayActivity.mp_bill_email, "payer.email@fiuu.com");
        paymentDetails.put(MOLPayActivity.mp_bill_mobile, "123456789");

        paymentDetails.put(MOLPayActivity.mp_extended_vcode, false); // Optional : Set true if your account enabled extended Verify Payment

        Intent intent = new Intent(CartActivity.this, ActivityGP.class); // Used ActivityGP for Google Pay
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        startActivityForResult(intent, MOLPayActivity.MOLPayXDK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("logGooglePay" , "onActivityResult requestCode = " + requestCode);
        Log.e("logGooglePay" , "onActivityResult resultCode = " + resultCode);

        if (resultCode == RESULT_OK){
                Log.d(MOLPayActivity.MOLPAY, "MOLPay result = " + data.getStringExtra(MOLPayActivity.MOLPayTransactionResult));
                //todo: redirect to success page
                Intent intent = new Intent(CartActivity.this, SuccessPayment.class);
                startActivity(intent);
        } else if (resultCode == RESULT_CANCELED) {
            Log.e("logGooglePay" , "RESULT_CANCELED exceed amount");
            Intent intent = new Intent(CartActivity.this, FailPayment.class);
            startActivity(intent);
        } else {
            Log.e("logGooglePay", "RESULT_CANCELED data == null");
            Intent intent = new Intent(CartActivity.this, FailPayment.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        // The Google Pay button is a layout file – take the root view
        googlePayButton = findViewById(R.id.googlePayButton);

        try {
            // TODO: Choose your preferred Google Pay button : https://developers.google.com/pay/api/android/guides/brand-guidelines
            googlePayButton.initialize(
                    ButtonOptions.newBuilder()
                            .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
                            .setButtonType(ButtonConstants.ButtonType.PAY)
                            .setCornerRadius(99)
                            .setAllowedPaymentMethods(UtilGP.getAllowedPaymentMethods().toString())
                            .build()
            );
            googlePayButton.setOnClickListener(view -> {
                googlePayPayment();
            });
        } catch (JSONException e) {
            // Keep Google Pay button hidden (consider logging this to your app analytics service)
        }

        cartView = findViewById(R.id.cartView);
        totalPriceTV = findViewById(R.id.totalPrice);

        ArrayList<String> selectedItems = getIntent().getStringArrayListExtra("selectedItems");

        itemList = new ArrayList<>();
        if (selectedItems != null && !selectedItems.isEmpty()) {
            for (String itemDetail : selectedItems) {
                String[] parts = itemDetail.split("-");
                if (parts.length == 3) {
                    String itemName = parts[0].trim();
                    int counter = Integer.parseInt(parts[1].trim());
                    double price = Double.parseDouble(parts[2].trim());

                    ItemModel item = new ItemModel(itemName, 0, price);
                    item.setCounter(counter);
                    itemList.add(item);
                }
            }

            cartAdapter = new CartAdapter((Context) this, itemList, (CartAdapter.OnItemChangeListener) this);
            cartView.setAdapter(cartAdapter);

            totalPriceTV.setText(String.format("RM %.2f", updateTotalPrice()));
        } else {
            totalPriceTV.setText("No items selected.");
        }
    }

    @Override
    public void onItemChanged() {
        updateTotalPrice();
    }
    private double updateTotalPrice() {
        totalPrice = 0;

        for (ItemModel item : itemList) {
            totalPrice += item.getItem_price() * item.getCounter();
        }

        totalPriceTV.setText(String.format("RM %.2f", totalPrice));

        totalPriceText = String.format("%.2f", totalPrice);

        return totalPrice;
    }

}
