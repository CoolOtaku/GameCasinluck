package com.denise_hill_sarah.gamecasinluck;

import static com.denise_hill_sarah.gamecasinluck.views_controllers.MainActivity.saveCOINS;
import static com.denise_hill_sarah.gamecasinluck.views_controllers.MainActivity.COINS;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class InAppClass implements PurchasesUpdatedListener {

    private Activity activity;
    private BillingClient billingClient;
    public List<SkuDetails> resList;

    public InAppClass(Activity activity) {
        this.activity = activity;

        billingClient = BillingClient.newBuilder(activity)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        startConnection();
    }

    private void startConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    getProductDetails();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                startConnection();
            }
        });
    }

    private void getProductDetails() {
        List<String> productsIdList = new ArrayList<>();
        productsIdList.add("com.denise_hill_sarah.gamecasinluck.buy_coins_25");
        productsIdList.add("com.denise_hill_sarah.gamecasinluck.buy_coins_50");
        productsIdList.add("com.denise_hill_sarah.gamecasinluck.buy_coins_100");
        productsIdList.add("com.denise_hill_sarah.gamecasinluck.buy_coins_150");
        SkuDetailsParams getProductsDetailsQuery = SkuDetailsParams
                .newBuilder()
                .setSkusList(productsIdList)
                .setType(BillingClient.SkuType.INAPP)
                .build();
        billingClient.querySkuDetailsAsync(getProductsDetailsQuery, new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                    resList = list;
                }
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                    handlePurchase(purchase);
                }
            }
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase != null) {
            if (purchase.getSkus().get(0).equals("com.denise_hill_sarah.gamecasinluck.buy_coins_150")) {
                COINS += 150;
            } else if (purchase.getSkus().get(0).equals("com.denise_hill_sarah.gamecasinluck.buy_coins_50")) {
                COINS += 50;
            } else if (purchase.getSkus().get(0).equals("com.denise_hill_sarah.gamecasinluck.buy_coins_100")) {
                COINS += 100;
            } else if (purchase.getSkus().get(0).equals("com.denise_hill_sarah.gamecasinluck.buy_coins_25")) {
                COINS += 25;
            }
            saveCOINS();
            activity.recreate();
        }
    }

    public BillingClient getBillingClient() {
        return billingClient;
    }

}