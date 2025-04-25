/*
 * Copyright 2023 Razer Merchant Services.
 */

package com.molpay.molpayxdk.googlepay;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.molpay.molpayxdk.googlepay.Helper.ApplicationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import okhttp3.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ApiRequestService {

    static class Production {
        static final String BASE_PAYMENT = "https://pay.merchant.razer.com/";
        static final String API_PAYMENT = "https://api.merchant.razer.com/";
    }

    static class Development {
        static final String BASE_PAYMENT = "https://sandbox.merchant.razer.com/";
        static final String API_PAYMENT = "https://sandbox.merchant.razer.com/";
    }

    private static String signature;
    private static Boolean extendedVcode;

    public ApiRequestService() {
    }

    // ### TODO : Get channels from API createTxn.php & pass result to getPaymentMethods()

    public interface NetworkCallback {
        void onSuccess(String responseJson);
        void onFailure(String error);
    }

    public static void CreateTxn(NetworkCallback callback , HashMap<String, Object> paymentDetails) {

        Log.e("logGooglePay", "CreateTxn");

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = null;

        if (paymentDetails != null) {

            Log.e("logGooglePay", "paymentDetails NOT NULL");

            if (paymentDetails.get("mp_extended_vcode") == null) {
                extendedVcode = false;
            } else {
                Log.e("logGooglePay", "mp_extended_vcode = " + paymentDetails.get("mp_extended_vcode"));
                extendedVcode = (Boolean) paymentDetails.get("mp_extended_vcode");
            }

            signature = ApplicationHelper.getInstance().GetVCode(
                    Objects.requireNonNull(paymentDetails.get("mp_amount")).toString(),
                    Objects.requireNonNull(paymentDetails.get("mp_merchant_ID")).toString(),
                    Objects.requireNonNull(paymentDetails.get("mp_order_ID")).toString(),
                    Objects.requireNonNull(paymentDetails.get("mp_verification_key")).toString(),
                    Objects.requireNonNull(paymentDetails.get("mp_currency")).toString(),
                    extendedVcode
            );

            Log.e("logGooglePay", "mp_amount = " + Objects.requireNonNull(paymentDetails.get("mp_amount")).toString());
            Log.e("logGooglePay", "mp_merchant_ID = " + Objects.requireNonNull(paymentDetails.get("mp_merchant_ID")).toString());
            Log.e("logGooglePay", "mp_order_ID = " + Objects.requireNonNull(paymentDetails.get("mp_order_ID")).toString());
            Log.e("logGooglePay", "mp_verification_key = " + Objects.requireNonNull(paymentDetails.get("mp_verification_key")).toString());
            Log.e("logGooglePay", "mp_currency = " + Objects.requireNonNull(paymentDetails.get("mp_currency")).toString());

            Log.e("logGooglePay", "mp_bill_name = " + Objects.requireNonNull(paymentDetails.get("mp_bill_name")).toString());
            Log.e("logGooglePay", "mp_bill_mobile = " + Objects.requireNonNull(paymentDetails.get("mp_bill_mobile")).toString());
            Log.e("logGooglePay", "signature = " + signature);


            FormBody.Builder formBuilder = new FormBody.Builder()
                    .add("MerchantID", Objects.requireNonNull(paymentDetails.get("mp_merchant_ID")).toString())
                    .add("ReferenceNo", Objects.requireNonNull(paymentDetails.get("mp_order_ID")).toString())
                    .add("TxnType", "SALS")
                    .add("TxnCurrency", Objects.requireNonNull(paymentDetails.get("mp_currency")).toString())
                    .add("TxnAmount", Objects.requireNonNull(paymentDetails.get("mp_amount")).toString())
                    .add("Signature", signature)
                    .add("CustName", Objects.requireNonNull(paymentDetails.get("mp_bill_name")).toString())
                    .add("CustContact", Objects.requireNonNull(paymentDetails.get("mp_bill_mobile")).toString())
                    .add("mpsl_version", "3")
                    .add("vc_channel", "indexAN")
                    .add("ReturnURL", "")
                    .add("NotificationURL", "")
                    .add("CallbackURL", "")
                    .add("ExpirationTime", "");

            // Handle paymentMethods[] from String[] mp_gpay_channel
            String[] gpayChannels = (String[]) paymentDetails.get("mp_gpay_channel");
            for (int i = 0; i < Objects.requireNonNull(gpayChannels).length; i++) {
                formBuilder.add("paymentMethods[" + i + "]", gpayChannels[i]);
            }

            formBody = formBuilder.build();

//            formBody = new FormBody.Builder()
//                    .add("MerchantID", "SB_molpayxdk")
//                    .add("ReferenceNo", "Ashraf_Testing-123")
//                    .add("TxnType", "SALS")
//                    .add("TxnCurrency", "MYR")
//                    .add("TxnAmount", "0.10")
//                    .add("Signature", "b1667822eeed9923b5d4737e9619bbbe")
//                    .add("CustName", "Ashraf Testing")
//                    .add("CustContact", "123456789")
//                    .add("mpsl_version", "3")
//                    .add("vc_channel", "indexAN")
//                    .add("ReturnURL", "")
//                    .add("NotificationURL", "")
//                    .add("CallbackURL", "")
//                    .add("ExpirationTime", "")
//                    .add("paymentMethods[0]", "CC")
//                    .add("paymentMethods[1]", "ShopeePay")
//                    .add("paymentMethods[2]", "TNG-EWALLET")
//                    .build();

            Request request = new Request.Builder()
                    .url("https://sandbox-payment.fiuu.com/RMS/GooglePay/createTxn.php")
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("logGooglePay", "onFailure = " + e.getMessage());
                    callback.onFailure(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("logGooglePay", "onResponse code = " + response.code());
                        callback.onFailure("Unexpected code: " + response.code());
                    } else {
                        String responseBody = response.body().string();
                        Log.e("logGooglePay", "onResponse responseBody = " + responseBody);
                        callback.onSuccess(responseBody);
                    }
                }
            });
        } else {
            Log.e("logGooglePay", "paymentDetails == NULL");
        }

    }

    public Object GetPaymentRequest(JSONObject paymentInput, String paymentInfo ) {

        try {
            String endPoint = "";
            String txnType = "SALS";
            String orderId = paymentInput.getString("orderId");
            String amount = paymentInput.getString("amount");
            String currency = paymentInput.getString("currency");
            boolean extendedVCode = paymentInput.getBoolean("extendedVCode");
            String billName = paymentInput.getString("billName");
            String billEmail = paymentInput.getString("billEmail");
            String billPhone = paymentInput.getString("billPhone");
            String billDesc = paymentInput.getString("billDesc");
            String merchantId = paymentInput.getString("merchantId");
            String verificationKey = paymentInput.getString("verificationKey");

            if (WebActivity.isSandbox.equals("false")) {
                endPoint = Production.BASE_PAYMENT + "RMS/GooglePay/payment_v2.php";
            } else if (WebActivity.isSandbox.equals("true")) {
                endPoint = "https://sandbox-payment.fiuu.com/RMS/GooglePay/payment_v2.php";
            }

//            if (WebActivity.isSandbox.equals("false")) {
//                endPoint = Production.BASE_PAYMENT + "RMS/API/Direct/1.4.0/index.php";
//            } else if (WebActivity.isSandbox.equals("true")) {
//                endPoint = Development.BASE_PAYMENT + "RMS/API/Direct/1.4.0/index.php";
//            }

            Uri uri = Uri.parse(endPoint)
                    .buildUpon()
                    .build();

            //"Signature": "<MD5(amount+merchantID+referenceNo+Vkey)>",
            String vCode = ApplicationHelper.getInstance().GetVCode(
                amount,
                merchantId,
                orderId,
                verificationKey,
                currency,
                extendedVCode
            );

            String GooglePayBase64 = Base64.getEncoder()
                                    .encodeToString(paymentInfo.getBytes());

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("MerchantID", merchantId)
                    .appendQueryParameter("ReferenceNo", orderId)
                    .appendQueryParameter("TxnType", txnType)
                    .appendQueryParameter("TxnCurrency", currency)
                    .appendQueryParameter("TxnAmount", amount)
                    .appendQueryParameter("CustName", billName)
                    .appendQueryParameter("CustEmail", billEmail)
                    .appendQueryParameter("CustContact", billPhone)
                    .appendQueryParameter("CustDesc", billDesc)
                    .appendQueryParameter("Signature", vCode)
                    .appendQueryParameter("mpsl_version", "2")
                    .appendQueryParameter("GooglePay", GooglePayBase64);

                return postRequest(uri, builder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object GetPaymentResult(JSONObject transaction ) {
        try {
            String endPoint = "";

            if (WebActivity.isSandbox.equals("false")) {
                endPoint = Production.API_PAYMENT + "RMS/q_by_tid.php";
            } else if (WebActivity.isSandbox.equals("true")) {
                endPoint = Development.API_PAYMENT + "RMS/q_by_tid.php";
            }

            Uri uri = Uri.parse(endPoint)
                    .buildUpon()
                    .build();

            String txID = transaction.getString("txID");
            String amount = transaction.getString("amount");
            String merchantId = transaction.getString("merchantId");
            String verificationKey = transaction.getString("verificationKey");

            String sKey = ApplicationHelper.getInstance().GetSKey(
                    txID,
                    merchantId,
                    verificationKey,
                    amount
            );

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("amount", amount)
                    .appendQueryParameter("txID", txID)
                    .appendQueryParameter("domain", merchantId)
                    .appendQueryParameter("skey", sKey)
                    .appendQueryParameter("url", "")
                    .appendQueryParameter("type", "2");

            return postRequest(uri, builder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject postRequest(final Uri uri, final Uri.Builder params) throws JSONException {

        HttpURLConnection httpConnection = null;
        try {

            URL url = new URL(uri.toString());
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Cookies", "PHPSESSID=ad6081qpihsb9en1nr9nivbkl3");
            httpConnection.setRequestProperty("SDK-Version", "4.0.0");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            String query = params.build().getEncodedQuery();

            Log.e("logGooglePay", "url = " + url.toString());
            Log.e("logGooglePay", "query = " + query.toString());

            OutputStream outputStream = httpConnection.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            writer.write(query);
            writer.flush();
            writer.close();

            outputStream.close();

            return parse(httpConnection);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject(String.format("{\"exception\":\"%s\"}", e.getMessage()));
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }

    }

    private JSONObject parse(HttpURLConnection httpURLConnection) throws JSONException {

        JSONObject response = new JSONObject();

        try {
            response.put("statusCode", httpURLConnection.getResponseCode());
            response.put("responseMessage", httpURLConnection.getResponseMessage());
            response.put("responseBody", getResponseBody(httpURLConnection));

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject(String.format("{\"exception\":\"%s\"}", e.getMessage()));
        }
    }

    public static String getResponseBody(HttpURLConnection conn) {

        BufferedReader br = null;
        StringBuilder body = null;
        String line = "";

        try {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            body = new StringBuilder();

            while ((line = br.readLine()) != null)
                body.append(line);

            return body.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}