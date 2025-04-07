package com.fiuu.xdkandroid;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wallet.button.ButtonConstants;
import com.google.android.gms.wallet.button.ButtonOptions;
import com.google.android.gms.wallet.button.PayButton;
import com.molpay.molpayxdk.MOLPayActivity;
import com.molpay.molpayxdk.googlepay.ActivityGP;
import com.molpay.molpayxdk.googlepay.UtilGP;

import org.json.JSONException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    HashMap<Object, Object> paymentDetails = new HashMap<>();
    AppCompatEditText etAmount;
    AppCompatButton btnVTDeeplink;
    Intent intentDeeplink;
    AppCompatTextView txtPaymentStatus, txtFullResponse;

    private void restartmolpay(String strAmount) {
        paymentDetails.put(MOLPayActivity.mp_amount, "1.10");

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

        paymentDetails.put(MOLPayActivity.mp_dev_mode, true);

        //------------------------------------------------------------------------------------------

//        // TODO: Enter your merchant account credentials before test run
//        paymentDetails.put(MOLPayActivity.mp_username, "");
//        paymentDetails.put(MOLPayActivity.mp_password, "");
//        paymentDetails.put(MOLPayActivity.mp_merchant_ID, "");
//        paymentDetails.put(MOLPayActivity.mp_app_name, "");
//        paymentDetails.put(MOLPayActivity.mp_verification_key, "");
//
//        paymentDetails.put(MOLPayActivity.mp_order_ID, Calendar.getInstance().getTimeInMillis());
//        paymentDetails.put(MOLPayActivity.mp_currency, "MYR");
//        paymentDetails.put(MOLPayActivity.mp_country, "MY");
//        paymentDetails.put(MOLPayActivity.mp_channel, "multi");
//        paymentDetails.put(MOLPayActivity.mp_bill_description, "bill description");
//        paymentDetails.put(MOLPayActivity.mp_bill_name, "bill name");
//        paymentDetails.put(MOLPayActivity.mp_bill_email, "example@gmail.com");
//        paymentDetails.put(MOLPayActivity.mp_bill_mobile, "123456789");

        // TODO: Learn more about optional parameters here https://github.com/RazerMS/Mobile-XDK-RazerMS_Android_Studio/wiki/Installation-Guidance#prepare-the-payment-detail-object
//        paymentDetails.put(MOLPayActivity.mp_extended_vcode, false); // For Google Pay Only - Set true if your account enabled extended Verify Payment
//        paymentDetails.put(MOLPayActivity.mp_channel_editing, false);
//        paymentDetails.put(MOLPayActivity.mp_editing_enabled, true);
//        paymentDetails.put(MOLPayActivity.mp_express_mode, false);
//        paymentDetails.put(MOLPayActivity.mp_dev_mode, false);
//        paymentDetails.put(MOLPayActivity.mp_closebutton_display, true);
//        paymentDetails.put(MOLPayActivity.mp_preferred_token, "new");

        openStartActivityResult();
    }

    private void openStartActivityResult(){
        Intent intent = new Intent(MainActivity.this, MOLPayActivity.class);
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        paymentActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> paymentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("MOLPAYXDKLibrary", "result: "+result);
                Log.d("MOLPAYXDKLibrary", "result: "+result.getResultCode());
                if (result.getResultCode() == MOLPayActivity.RESULT_OK) {
                    Log.d("MOLPAYXDKLibrary", "result: "+ result.getData().getStringExtra(MOLPayActivity.MOLPayTransactionResult));

                    TextView tw = findViewById(R.id.resultTV);
                    tw.setText(result.getData().getStringExtra(MOLPayActivity.MOLPayTransactionResult));
                }

            }
    );


    private void googlePayPayment() {
        paymentDetails = new HashMap<>();

        /*
            TODO: Follow Google’s instructions to request production access for your app: https://developers.google.com/pay/api/android/guides/test-and-deploy/request-prod-access
            *
             Choose the integration type Gateway when prompted, and provide screenshots of your app for review.
             After your app has been approved, test your integration in production by set mp_sandbox_mode = false & use production mp_verification_key & mp_merchant_ID.
             Then launching Google Pay from a signed, release build of your app.
             */
        paymentDetails.put(MOLPayActivity.mp_sandbox_mode, true); // Only set to false once you have request production access for your app

        // TODO: Enter your merchant account credentials before test run
        paymentDetails.put(MOLPayActivity.mp_merchant_ID, ""); // Your sandbox / production merchant ID
        paymentDetails.put(MOLPayActivity.mp_verification_key, ""); // Your sandbox / production verification key

        paymentDetails.put(MOLPayActivity.mp_amount, "1.01"); // Must be in 2 decimal points format
        paymentDetails.put(MOLPayActivity.mp_order_ID, Calendar.getInstance().getTimeInMillis()); // Must be unique
        paymentDetails.put(MOLPayActivity.mp_currency, "MYR"); // Must matched mp_country
        paymentDetails.put(MOLPayActivity.mp_country, "MY"); // Must matched mp_currency
        paymentDetails.put(MOLPayActivity.mp_bill_description, "The bill description");
        paymentDetails.put(MOLPayActivity.mp_bill_name, "The bill name");
        paymentDetails.put(MOLPayActivity.mp_bill_email, "payer.email@fiuu.com");
        paymentDetails.put(MOLPayActivity.mp_bill_mobile, "123456789");

        paymentDetails.put(MOLPayActivity.mp_extended_vcode, false); // Optional : Set true if your account enabled extended Verify Payment
        openGPActivityWithResult();

    }


    private void openGPActivityWithResult() {
        Intent intent = new Intent(MainActivity.this, ActivityGP.class); // Used ActivityGP for Google Pay
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        gpActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> gpActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("MOLPAYXDKLibrary", "result: "+result);
                Log.d("MOLPAYXDKLibrary", "result: "+result.getResultCode());

                if (result.getResultCode() == MOLPayActivity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String transactionResult = data.getStringExtra(MOLPayActivity.MOLPayTransactionResult);

                    if (transactionResult != null) {
                        Log.d(MOLPayActivity.MOLPAY, "MOLPay result = " + data.getStringExtra(MOLPayActivity.MOLPayTransactionResult));
                        TextView tw = findViewById(R.id.resultTV);
                        tw.setText(data.getStringExtra(MOLPayActivity.MOLPayTransactionResult));
                    }
                } else {
                    Log.e("logGooglePay" , "RESULT_CANCELED data == null");
                    TextView tw = findViewById(R.id.resultTV);
                    tw.setText("result = null");
                }
            }
    );

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
            if (Objects.requireNonNull(etAmount.getText()).toString().isEmpty()) {
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                restartmolpay(Objects.requireNonNull(etAmount.getText()).toString());
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
