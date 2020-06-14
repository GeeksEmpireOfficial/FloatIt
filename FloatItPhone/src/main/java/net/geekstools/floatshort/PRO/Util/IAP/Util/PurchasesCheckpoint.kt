/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 3/8/20 7:23 AM
 * Last modified 3/8/20 7:23 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geekstools.floatshort.PRO.Util.IAP.Util

import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug.Companion.PrintDebug
import net.geekstools.floatshort.PRO.Util.IAP.billing.BillingManager

class PurchasesCheckpoint(var appCompatActivity: AppCompatActivity) {

    val functionsClass: FunctionsClass = FunctionsClass(appCompatActivity)

    fun trigger() : BillingClient {

        val billingClient = BillingClient.newBuilder(appCompatActivity).setListener { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchaseList != null) {

            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {

            } else {

            }
        }.enablePendingPurchases().build()

        //In-App Billing
        if (functionsClass.networkConnection()) {

            //Restore Purchased Item
            billingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingSetupFinished(billingResult: BillingResult) {

                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                        val purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList

                        if (purchases != null) {
                            for (purchase in purchases) {
                                PrintDebug("*** Purchased Item: $purchase ***")

                                functionsClass.savePreference(".PurchasedItem", purchase.sku, true)

                                //Consume Donation
                                if (purchase.sku == BillingManager.iapDonation
                                        && functionsClass.alreadyDonated()) {

                                    val consumeResponseListener = ConsumeResponseListener { billingResult, purchaseToken ->
                                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                            PrintDebug("*** Consumed Item: $purchaseToken ***")

                                            functionsClass.savePreference(".PurchasedItem", purchase.sku, false)
                                        }
                                    }
                                    val consumeParams = ConsumeParams.newBuilder()
                                    consumeParams.setPurchaseToken(purchase.purchaseToken)
                                    billingClient.consumeAsync(consumeParams.build(), consumeResponseListener)
                                }

                                purchaseAcknowledgeProcess(billingClient, purchase, BillingClient.SkuType.INAPP)
                            }
                        }
                    }
                }

                override fun onBillingServiceDisconnected() {

                }
            })

            //Restore Subscribed Item
            billingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingSetupFinished(billingResult: BillingResult) {

                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        functionsClass.savePreference(".SubscribedItem", BillingManager.iapRemoveAds, false)

                        val purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS).purchasesList
                        if (purchases != null) {
                            for (purchase in purchases) {
                                PrintDebug("*** Subscribed Item: $purchase ***")

                                functionsClass.savePreference(".SubscribedItem", purchase.sku, true)

                                purchaseAcknowledgeProcess(billingClient, purchase, BillingClient.SkuType.SUBS)
                            }
                        }
                    }
                }

                override fun onBillingServiceDisconnected() {

                }
            })
        }

        return billingClient
    }

    private fun purchaseAcknowledgeProcess(billingClient: BillingClient, purchase: Purchase, purchaseType: String) = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            PrintDebug("*** ${purchase.sku} Purchase Acknowledged: ${purchase.isAcknowledged} ***")

            if (!purchase.isAcknowledged) {

                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)

                val aPurchaseResult: BillingResult = billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())

                PrintDebug("*** Purchased Acknowledged Result: ${purchase.sku} -> ${aPurchaseResult.debugMessage} ***")
            }
        }
    }
}