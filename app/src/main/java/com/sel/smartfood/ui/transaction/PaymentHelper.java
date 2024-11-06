package com.sel.smartfood.ui.transaction;

import android.app.Activity;
import android.content.Context;
import com.android.billingclient.api.*;

import java.util.ArrayList;
import java.util.List;

public class PaymentHelper<Activity extends android.app.Activity> implements PurchasesUpdatedListener {
    private BillingClient billingClient;
    private Object context;

    public PaymentHelper(Context context) {
        billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // BillingClient sẵn sàng
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Xử lý khi mất kết nối
            }
        });
    }

    public void initiatePurchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add("dollar_pay01"); // ID của sản phẩm giá 9.99$

        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build();

        billingClient.querySkuDetailsAsync(params, (billingResult, skuDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                for (SkuDetails skuDetails : skuDetailsList) {
                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails)
                            .build();
                    billingClient.launchBillingFlow((Activity) context, flowParams);
                }
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                // Xử lý giao dịch thành công ở đây
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Người dùng hủy giao dịch
        } else {
            // Lỗi khác
        }
    }
}

