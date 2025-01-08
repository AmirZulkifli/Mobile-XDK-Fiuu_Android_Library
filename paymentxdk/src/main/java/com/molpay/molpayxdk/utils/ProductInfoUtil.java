package com.molpay.molpayxdk.utils;

import com.molpay.molpayxdk.BuildConfig;

public class ProductInfoUtil {

    public static ProductInfo getProductInfo() {
        String type = "XDKA";
        String version = BuildConfig.XDKAVersion;

        return new ProductInfo(type,version);
    }
}
