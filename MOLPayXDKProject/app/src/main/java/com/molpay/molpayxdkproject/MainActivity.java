package com.molpay.molpayxdkproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.molpay.molpayxdk.MOLPayActivity;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private void restartmolpay() {
        HashMap<String, Object> paymentDetails = new HashMap<>();
        paymentDetails.put(MOLPayActivity.mp_amount, "1.01");

        paymentDetails.put(MOLPayActivity.mp_username, "RMSxdk_2022");
        paymentDetails.put(MOLPayActivity.mp_password, "RMSpwd@2022");
        paymentDetails.put(MOLPayActivity.mp_merchant_ID, "rmsxdk_mobile_Dev");
        paymentDetails.put(MOLPayActivity.mp_app_name, "mobile");
        paymentDetails.put(MOLPayActivity.mp_verification_key, "ee738b541eff7b6b495e44771f71c0ec");

        paymentDetails.put(MOLPayActivity.mp_order_ID, Calendar.getInstance().getTimeInMillis());
        paymentDetails.put(MOLPayActivity.mp_currency, "MYR");
        paymentDetails.put(MOLPayActivity.mp_country, "MY");
        paymentDetails.put(MOLPayActivity.mp_channel, "multi");
        paymentDetails.put(MOLPayActivity.mp_bill_description, "bill description");
        paymentDetails.put(MOLPayActivity.mp_bill_name, "bill name");
        paymentDetails.put(MOLPayActivity.mp_bill_email, "example@gmail.com");
        paymentDetails.put(MOLPayActivity.mp_bill_mobile, "01234567888");
        paymentDetails.put(MOLPayActivity.mp_channel_editing, false);
        paymentDetails.put(MOLPayActivity.mp_editing_enabled, true);

        // 3.23.0
//        paymentDetails.put(MOLPayActivity.mp_bill_name, "Clement`~!@#$%^&*()_-+={[}]|\\\\:;\\\"'<,>.?/");

//        paymentDetails.put(MOLPayActivity.mp_transaction_id, "");//8064815
//        paymentDetails.put(MOLPayActivity.mp_request_type, "");

//        String binlock[] = {"123456","234567"};
//        paymentDetails.put(MOLPayActivity.mp_bin_lock, binlock);
//        paymentDetails.put(MOLPayActivity.mp_bin_lock_err_msg, "Wrong BIN format");
//
//        paymentDetails.put(MOLPayActivity.mp_is_escrow, "");
//        paymentDetails.put(MOLPayActivity.mp_filter, "1");
//        paymentDetails.put(MOLPayActivity.mp_custom_css_url, "file:///android_asset/custom.css");
//        paymentDetails.put(MOLPayActivity.mp_preferred_token, "");
//        paymentDetails.put(MOLPayActivity.mp_tcctype, "SALS");//SALS //AUTH
//        paymentDetails.put(MOLPayActivity.mp_is_recurring, false);

//        String allowedChannels[] = {"credit","credit3"};
//        paymentDetails.put(MOLPayActivity.mp_allowed_channels, allowedChannels);

//        paymentDetails.put(MOLPayActivity.mp_sandbox_mode, true);
//        paymentDetails.put(MOLPayActivity.mp_express_mode, true);
//        paymentDetails.put(MOLPayActivity.mp_advanced_email_validation_enabled, true);
//        paymentDetails.put(MOLPayActivity.mp_advanced_phone_validation_enabled, true);
//        paymentDetails.put(MOLPayActivity.mp_bill_name_edit_disabled, true);
//        paymentDetails.put(MOLPayActivity.mp_bill_email_edit_disabled, true);
//        paymentDetails.put(MOLPayActivity.mp_bill_mobile_edit_disabled, true);
//        paymentDetails.put(MOLPayActivity.mp_bill_description_edit_disabled, true);
//        paymentDetails.put("is_submodule", true);
//        paymentDetails.put("module_id", "abc.com");
//        paymentDetails.put("wrapper_version", "99");
//        paymentDetails.put(MOLPayActivity.mp_dev_mode, true);
//        paymentDetails.put(MOLPayActivity.mp_language, "ZH");
//        paymentDetails.put("mp_timeout", 300);
//        paymentDetails.put(MOLPayActivity.mp_cash_waittime, 24);
//        paymentDetails.put(MOLPayActivity.mp_non_3DS, true);
//        paymentDetails.put(MOLPayActivity.mp_card_list_disabled, true);

//        String disabledChannels[] = {"credit"};
//        paymentDetails.put(MOLPayActivity.mp_disabled_channels, disabledChannels);

        Intent intent = new Intent(MainActivity.this, MOLPayActivity.class);
        intent.putExtra(MOLPayActivity.MOLPayPaymentDetails, paymentDetails);
        startActivityForResult(intent, MOLPayActivity.MOLPayXDK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

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
            restartmolpay();
        }

        return super.onOptionsItemSelected(item);
    }
}
