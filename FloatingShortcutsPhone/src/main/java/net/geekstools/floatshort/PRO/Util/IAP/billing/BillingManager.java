package net.geekstools.floatshort.PRO.Util.IAP.billing;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.IAP.InAppBilling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BillingManager implements PurchasesUpdatedListener {

    private static final String TAG = "BillingManager";

    FunctionsClass functionsClass;

    private BillingClient billingClient;
    private Activity activity;

    String UserEmailAddress = null;

    private static final HashMap<String, List<String>> SKUS;

    static {
        SKUS = new HashMap<>();
        SKUS.put(BillingClient.SkuType.INAPP, Arrays.asList("donation"));
        SKUS.put(BillingClient.SkuType.SUBS, Arrays.asList("remove.ads"));
    }

    public List<String> getSkus(@BillingClient.SkuType String type) {
        return SKUS.get(type);
    }

    public BillingManager(Activity activity, String UserEmailAddress) {

        this.activity = activity;
        this.UserEmailAddress = UserEmailAddress;

        functionsClass = new FunctionsClass(activity.getApplicationContext(), activity);

        billingClient = BillingClient.newBuilder(this.activity).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponse) {
                if (billingResponse == BillingClient.BillingResponse.OK) {
                    List<Purchase> purchasesItems = billingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
                    for (Purchase purchase : purchasesItems) {
                        FunctionsClass.println("*** Purchased Item: " + purchase + " ***");
                        functionsClass.savePreference(".PurchasedItem", purchase.getSku(), true);
                    }

                    List<Purchase> subscribedItem = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
                    for (Purchase purchase : subscribedItem) {
                        FunctionsClass.println("*** Subscribed Item: " + purchase + " ***");
                        functionsClass.savePreference(".SubscribedItem", purchase.getSku(), true);
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }

    public int startPurchaseFlow(SkuDetails skuDetails, String skuId, String billingType) {
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .setAccountId(UserEmailAddress)
                .build();

        return billingClient.launchBillingFlow(activity, billingFlowParams);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        //ResponseCode 7 = Item Owned
        Log.d(TAG, "onPurchasesUpdated() Response: " + responseCode);

        activity.finish();
        activity.startActivity(new Intent(activity.getApplicationContext(), InAppBilling.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void querySkuDetailsAsync(@BillingClient.SkuType final String itemType, final List<String> skuList, final SkuDetailsResponseListener listener) {
        SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(itemType).build();
        billingClient.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                listener.onSkuDetailsResponse(responseCode, skuDetailsList);
            }
        });
    }
}
