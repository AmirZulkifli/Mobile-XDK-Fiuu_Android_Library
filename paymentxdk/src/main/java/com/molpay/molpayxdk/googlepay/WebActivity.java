/*
 * Copyright 2023 Razer Merchant Services.
 */

package com.molpay.molpayxdk.googlepay;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.molpay.molpayxdk.R;
import com.molpay.molpayxdk.googlepay.Helper.RMSGooglePay;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class WebActivity extends AppCompatActivity {

    private WebView wvGateway;
    private ProgressBar pbLoading;
    private AppCompatTextView tvLoading;
    public Transaction transaction = new Transaction();

    public static boolean statCodeValueSuccess = false;

    public static String isSandbox = "";

    private CountDownTimer countDownTimer;
    private String requestType = "";
    private JSONObject lastestPaymentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("logGooglePay" , "WebActivity");

        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        String paymentInput = intent.getStringExtra("paymentInput");
        String paymentInfo = intent.getStringExtra("paymentInfo");

        Log.e("logGooglePay" , "after getStringExtra 1");

        if (paymentInput != null) {
            // Transcation model from paymentInput
            JSONObject paymentInputObj = null;
            try {
                paymentInputObj = new JSONObject(paymentInput);
                transaction.setVkey(paymentInputObj.getString("verificationKey"));
                isSandbox = paymentInputObj.getString("isSandbox");
                Log.e("logGooglePay" , "WebActivity isSandbox = " + isSandbox);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        tvLoading = findViewById(R.id.tvLoading);
        pbLoading = findViewById(R.id.pbLoading);
        wvGateway = findViewById(R.id.webView);
        wvGateway.setBackgroundColor(Color.WHITE);
        wvGateway.getSettings().setDomStorageEnabled(true);
        wvGateway.getSettings().setJavaScriptEnabled(true);
        wvGateway.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wvGateway.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvGateway.getSettings().setSupportMultipleWindows(true);

        // Mobile web view settings
        wvGateway.getSettings().setLoadWithOverviewMode(true);
        wvGateway.getSettings().setUseWideViewPort(true);
        wvGateway.getSettings().setSupportZoom(true);
        wvGateway.getSettings().setBuiltInZoomControls(true);
        wvGateway.getSettings().setDisplayZoomControls(false);

        Log.e("logGooglePay" , "before get cancelResponse");

        String cancelResponse = intent.getStringExtra("cancelResponse");

        if (cancelResponse != null) {
            Log.e("logGooglePay" , "cancelResponse != null");

            try {
                // Convert the JSON string into a JSONObject
                JSONObject responseBody = new JSONObject(cancelResponse);
                Log.e("logGooglePay", "-1 set minTimeOut 60000");
                ActivityGP.minTimeOut = 60000;
                onRequestData(responseBody);
                Log.e("logGooglePay" , "cancelResponse = " + cancelResponse);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e("logGooglePay" , "bypass return cancelResponse");

        new Thread(() -> {
            PaymentThread paymentThread = new PaymentThread();
            paymentThread.setValue(paymentInput, paymentInfo);
            paymentThread.run(); // or use thread.start() and thread.join() if needed

            try {
                JSONObject paymentResult = new JSONObject(new JSONObject(paymentThread.getValue()).getString("responseBody"));

                // Update UI
                runOnUiThread(() -> {
                    Log.e("logGooglePay", "thread paymentResult = " + paymentResult);
                    lastestPaymentResult = paymentResult;
                    Log.e("logGooglePay", "0 set minTimeOut 60000");
                    ActivityGP.minTimeOut = 60000;
                    onRequestData(paymentResult); // <-- UI-related logic
                });

            } catch (JSONException e) {
                runOnUiThread(() -> {
                    Log.e("logGooglePay", "JSONException = " + e);
                    Intent resultCancel = new Intent();
                    resultCancel.putExtra("response", String.valueOf(e));
                    setResult(RESULT_CANCELED, resultCancel);
                    finish();
                });
            }
        }).start();


        // Register a callback for handling the back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing - prevent user from performing backpress
                Log.e("logGooglePay" , "WebActivity GP backpressed");
            }
        };

        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void onStartTimOut() {

        long interval = 3000;
        final String[] queryResultStr = {null};
        final String[] trasactionJsonStr = {null};

        Log.e("logGooglePay" , "onStartTimOut ActivityGP.minTimeOut = " + ActivityGP.minTimeOut);

        // Query Transaction ID for every 3 second in 1 minute
        countDownTimer = new CountDownTimer(ActivityGP.minTimeOut, interval) {

            @Override
            public void onTick(long millisUntilFinished) {

                Log.e("logGooglePay" , "onTick millisUntilFinished = " + millisUntilFinished);

                JSONObject transactionObject = new JSONObject();
                try {
                    transactionObject.put("txID", transaction.getTxID());
                    transactionObject.put("amount", transaction.getAmount());
                    transactionObject.put("merchantId", transaction.getDomain());
                    transactionObject.put("verificationKey", transaction.getVkey());
                    trasactionJsonStr[0] = transactionObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("logGooglePay" , "trasactionJsonStr[0] = " + trasactionJsonStr[0]);

                QueryResultThread queryResultThread = new QueryResultThread();
                queryResultThread.setValue(trasactionJsonStr[0]); // set value

                Thread thread = new Thread(queryResultThread);
                thread.start();

                try {
                    Log.e("logGooglePay" , "try CountDownTimer");

                    Log.e("logGooglePay" , "queryResultThread.resp = " + queryResultThread.resp);
                    Log.e("logGooglePay" , "queryResultThread.transaction = " + queryResultThread.transaction);

                    thread.join();
                    queryResultStr[0] = queryResultThread.getValue();

                    Log.e("logGooglePay" , "queryResultStr[0] = " + queryResultStr[0]);

                    if (queryResultStr[0] != null) {
                        Log.e("logGooglePay" , "1");
                        try {
                            Log.e("logGooglePay" , "try 2");
                            JSONObject queryResultObj = new JSONObject(queryResultStr[0]);
                            String responseBody = queryResultObj.getString("responseBody");
                            JSONObject responseBodyObj = new JSONObject(responseBody);

                            Log.e("logGooglePay" , "responseBodyObj = " + responseBodyObj);

                            // If StatCode
                            if (responseBodyObj.has("StatCode")){
                                String statCodeValue = responseBodyObj.getString("StatCode");
                                String channelValue = responseBodyObj.getString("Channel");

//                                For Testing User Case Only
//                                if (millisUntilFinished < 50000) {
//                                    statCodeValue = "00";
//                                }

                                Log.e("logGooglePay" , "statCodeValue = " + statCodeValue);
                                Log.e("logGooglePay" , "channelValue = " + channelValue);

                                if (statCodeValue.equals("00")) {
                                    Log.e("logGooglePay" , "statCodeValueSuccess " + statCodeValueSuccess);
                                    if (statCodeValueSuccess) {
                                        Log.e("logGooglePay" , "statCodeValueSuccess finish");
                                        onFinish();
                                    }
                                } else if (statCodeValue.equals("11")) {
                                    cancel();
                                    pbLoading.setVisibility(View.GONE);
                                    tvLoading.setVisibility(View.GONE);

                                    String errorCode = null;
                                    try {
                                        errorCode = responseBodyObj.getString("ErrorCode");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    String errorDesc = null;
                                    try {
                                        errorDesc = responseBodyObj.getString("ErrorDesc");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    new AlertDialog.Builder(WebActivity.this)
                                            .setTitle("Payment Failed")
                                            .setMessage(errorCode + " : " + errorDesc)
                                            .setCancelable(false)
                                            .setPositiveButton("CLOSE", (dialog, which) -> {
                                                Log.e("logGooglePay" , "RESULT_CANCELED WebActivity 1 responseBodyObj = " + responseBodyObj);
                                                Intent resultCancel = new Intent();
                                                resultCancel.putExtra("response", String.valueOf(responseBodyObj));
                                                setResult(RESULT_CANCELED, resultCancel);
                                                finish();
                                            }).show();
                                }  else if (statCodeValue.equals("22")) {
                                    if (channelValue.contains("ShopeePay") || channelValue.contains("TNG-EWALLET")) {
                                        Log.e("logGooglePay" , "E-Wallet - need requery payment_v2");
                                        countDownTimer.cancel();
                                        if (millisUntilFinished > 3000) {
                                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                                // TODO 2 : Fix visibility kelip2
                                                pbLoading.setVisibility(View.VISIBLE);
                                                tvLoading.setVisibility(View.VISIBLE);
                                                ActivityGP.minTimeOut = ActivityGP.minTimeOut - 3000;
                                                onRequestData(lastestPaymentResult);
                                            }, 3000); // 3-second delay
                                        } else {
                                            // TODO 2 : Fix cancel timeout 22
                                            Intent resultCancel = new Intent();
                                            resultCancel.putExtra("response", String.valueOf(responseBodyObj));
                                            setResult(RESULT_CANCELED, resultCancel);
                                            finish();
                                        }
                                    } else {
                                        // Do Nothing - It will auto handle q_by_tid.php
                                        Log.e("logGooglePay" , "CARD - Do Nothing it will auto handle by q_by_tid.php");
                                    }
                                }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    Log.e("logGooglePay" , "InterruptedException = " + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {

                Log.e("logGooglePay" , "onFinish");

                try {
                    Log.e("logGooglePay" , "try WA 1");

                    JSONObject queryResultObj = new JSONObject(queryResultStr[0]);
                    Log.e("logGooglePay" , "1");
                    String responseBody = queryResultObj.getString("responseBody");
                    Log.e("logGooglePay" , "2 responseBody = " + responseBody);
                    JSONObject responseBodyObj = new JSONObject(responseBody);
                    Log.e("logGooglePay" , "3");
                    Intent intent = new Intent();
                    Log.e("logGooglePay" , "4");
                    intent.putExtra("response", String.valueOf(responseBodyObj));

                    Log.e("logGooglePay" , "onFinish response = " + String.valueOf(responseBodyObj));
                    Log.e("logGooglePay" , "StatCode = " + responseBodyObj.getString("StatCode"));

                    // If timeout / cancel
                    if (!responseBodyObj.has("StatCode")){
                        // Redirect to cancel flow
                        Log.e("logGooglePay" , "RESULT_CANCELED WebActivity 2 responseBodyObj = " + responseBodyObj);
                        setResult(RESULT_CANCELED, intent);
                    } else {
                        setResult(RESULT_OK, intent);
                    }

                    countDownTimer.cancel();
                    Log.e("logGooglePay" , "last");
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("logGooglePay" , "RESULT_CANCELED 0 JSONException = " + e);
                }
            }
        };
        countDownTimer.start();
    }

    private String xdkHTMLRedirection = "";
    private void onLoadHtmlWebView(String plainHtml) {

//        wvGateway.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.VISIBLE);
        statCodeValueSuccess = false;

        String encodedHtml = Base64.encodeToString(plainHtml.getBytes(), Base64.NO_PADDING);

        Log.e("logGooglePay" , "plainHtml = " + plainHtml);

        if (plainHtml.contains("xdkHTMLRedirection")) {
            xdkHTMLRedirection = StringUtils.substringBetween(plainHtml, "xdkHTMLRedirection' value='", "'");
            wvGateway.loadData(xdkHTMLRedirection, "text/html", "base64");
        } else if (requestType.equalsIgnoreCase("REDIRECT")) {
            wvGateway.loadData(encodedHtml, "text/html", "base64");
            pbLoading.setVisibility(View.GONE);
            tvLoading.setVisibility(View.GONE);
            wvGateway.setVisibility(View.VISIBLE);
        } else {
            wvGateway.loadData(encodedHtml, "text/html", "base64");
        }

        wvGateway.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                Log.e("logGooglePay" , "shouldOverrideUrlLoading = " + request.getUrl());

                if (request.getUrl().toString().contains("result.php")) {
                    statCodeValueSuccess = true;
                    pbLoading.setVisibility(View.VISIBLE);
                    tvLoading.setVisibility(View.VISIBLE);
                    wvGateway.setVisibility(View.GONE);
                }

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    public void onRequestData(JSONObject response) {

        Log.e("logGoogle" , "onRequestData = " + response);

        try {
            Log.e("logGoogle" , "try WA 2");

            if (response.has("responseBody")) {
                Log.e("logGoogle" , "test has responseBody");
            }

            if (response.has("error_code") && response.has("error_desc")) {
                Log.e("logGoogle" , "1");
                Intent intent = new Intent();
                String strResponse = response.toString();
                intent.putExtra("response", strResponse);
                // Redirect to cancel flow
                Log.e("logGooglePay" , "RESULT_CANCELED WebActivity 3 strResponse = " + strResponse);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
            if (response.has("TxnID")) {

                Log.e("logGoogle" , "2");

                try {
                    Log.e("logGoogle" , "try 2 ActivityGP.verificationKey = " + ActivityGP.verificationKey);
                    transaction.setTxID(response.getString("TxnID"));
                    transaction.setDomain(response.getString("MerchantID"));
                    transaction.setAmount(response.getString("TxnAmount"));
                    transaction.setVkey(ActivityGP.verificationKey);
                } catch (JSONException e) {
                    Log.e("logGoogle" , "JSONException 3 = " + e);
                    e.printStackTrace();
                }

                if (response.has("TxnData") && !response.has("pInstruction")) {

                    Log.e("logGoogle" , "3");

                    onStartTimOut();

                    JSONObject txnData = response.getJSONObject("TxnData");

                    StringBuilder html = new StringBuilder();
                    html.append(String.format("<form id='prForm' action='%s' method='%s'>\n",
                            txnData.getString("RequestURL"),
                            txnData.getString("RequestMethod"))
                    );
                    if (txnData.has("AppDeepLinkURL")) {
//                        AppData.getInstance().setRedirectAppUrl(txnData.getString("AppDeepLinkURL"));
                    }
                    if (txnData.has("RequestType")) {
                        requestType = txnData.getString("RequestType");
                    }
                    if (txnData.has("RequestData")) {

                        if (txnData.get("RequestData") instanceof JSONObject) {
                            JSONObject requestData = txnData.getJSONObject("RequestData");

                            Iterator<String> keys = requestData.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();

                                if (requestData.get(key) instanceof JSONObject) {
                                    // Do nothing
                                } else {
                                    if (requestData.has("checkoutUrl")) {
//                                        AppData.getInstance().setRedirectAppUrl(requestData.getString("checkoutUrl"));
                                    }
                                    html.append(String.format("<input type='hidden' name='%s' value='%s'>\n", key, requestData.getString(key)));
                                }
                            }
                        }
                    }

                    html.append("</form>");
                    html.append("<script> document.getElementById('prForm').submit();</script>");

                    onLoadHtmlWebView(html.toString());
                } else {
                    Intent intent = new Intent();
                    String strResponse = response.toString();
                    intent.putExtra("response", strResponse);
                    // Redirect to cancel flow
                    Log.e("logGooglePay" , "RESULT_CANCELED WebActivity 4 strResponse = " + strResponse);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            } else {
                Log.e("logGoogle" , "cancel - else");
                Intent intent = new Intent();
                String strResponse = response.toString();
                intent.putExtra("response", strResponse);
                // Redirect to cancel flow
                Log.e("logGooglePay" , "RESULT_CANCELED WebActivity 5 = " + strResponse);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        } catch (JSONException e) {
            Log.e("logGoogle" , "onRequestData JSONException = " + e);
            e.printStackTrace();
        }
    }

    public class PaymentThread implements Runnable {
        private volatile String resp;
        private String paymentInput;
        private String  paymentInfo;

        public String getValue() {
            return resp;
        }

        public void setValue(String paymentInput, String  paymentInfo) {
            this.paymentInput = paymentInput;
            this.paymentInfo = paymentInfo;
        }

        @Override
        public void run() {
            RMSGooglePay pay = new RMSGooglePay();
            JSONObject result;
            result = (JSONObject) pay.requestPayment(paymentInput, paymentInfo);
            resp = result.toString();
        }
    }

    public class QueryResultThread implements Runnable {
        private volatile String resp;
        private String transaction;

        public String getValue() {
            return resp;
        }

        public void setValue(String transaction) {
            this.transaction = transaction;
        }

        @Override
        public void run() {

                Log.e("logGooglePay" , "QueryResultThread run");

                RMSGooglePay pay = new RMSGooglePay();
                JSONObject result = (JSONObject) pay.queryPaymentResult(transaction);

                Log.e("logGooglePay" , "result = " + result);

                if (result != null) {
                    resp = result.toString();
                }
        }
    }

}