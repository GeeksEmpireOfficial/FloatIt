package net.geekstools.floatshort.PRO.Util.IAP;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.material.button.MaterialButton;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClassDebug;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.IAP.billing.BillingProvider;
import net.geekstools.floatshort.PRO.Util.IAP.skulist.SkusAdapter;
import net.geekstools.floatshort.PRO.Util.IAP.skulist.row.SkuRowData;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class AcquireFragment extends DialogFragment implements View.OnClickListener {

    Activity activity;
    Context context;

    FunctionsClass functionsClass;

    RecyclerView recyclerView;
    ProgressBar progressBar;

    HorizontalScrollView itemDemo;
    LinearLayout itemDemoList;
    TextView itemDemoDescription;

    SkusAdapter skusAdapter;

    MaterialButton materialButtonShare;

    BillingProvider billingProvider;

    TreeMap<Integer, Drawable> mapIndexDrawable = new TreeMap<Integer, Drawable>();
    TreeMap<Integer, Uri> mapIndexURI = new TreeMap<Integer, Uri>();

    RequestManager requestManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = getActivity();
        this.context = getContext();

        functionsClass = new FunctionsClass(context, activity);
        requestManager = Glide.with(context);

        if (PublicVariable.themeLightDark) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.AppThemeLight);
        } else {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.AppThemeDark);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.iap_fragment, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        progressBar = (ProgressBar) root.findViewById(R.id.progress_circular);
        itemDemo = (HorizontalScrollView) root.findViewById(R.id.itemDemo);
        itemDemoList = (LinearLayout) root.findViewById(R.id.itemDemoList);
        itemDemoDescription = (TextView) root.findViewById(R.id.itemDemoDescription);
        materialButtonShare = (MaterialButton) root.findViewById(R.id.shareNow);

        root.findViewById(R.id.backgroundFull).setBackgroundColor(PublicVariable.themeLightDark ? context.getColor(R.color.light) : context.getColor(R.color.dark));

        onManagerReady((BillingProvider) activity);

        materialButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareText =
                        getString(R.string.shareTitle) +
                                "\n" + getString(R.string.shareSummary) +
                                "\n" + getString(R.string.play_store_link) + getContext().getPackageName();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sharingIntent.setType("text/plain");
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(sharingIntent);
            }
        });

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, android.view.KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    requestManager.pauseAllRequests();
                    activity.finish();
                }
                return true;
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            requestManager.resumeRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            requestManager.pauseAllRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ImageView) {
            String screenshotURI = view.getTag().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(screenshotURI));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void refreshUI() {
        if (skusAdapter != null) {
            skusAdapter.notifyDataSetChanged();
        }
    }

    public void onManagerReady(BillingProvider billingProvider) {
        this.billingProvider = billingProvider;
        if (recyclerView != null) {
            skusAdapter = new SkusAdapter(this.billingProvider, activity);
            if (recyclerView.getAdapter() == null) {
                recyclerView.setAdapter(skusAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            }
            handleManagerAndUiReady();
        }
    }

    private void handleManagerAndUiReady() {
        List<String> inAppSkus = billingProvider.getBillingManager().getSkus(BillingClient.SkuType.INAPP);
        List<SkuRowData> skuRowDataList = new ArrayList<>();
        billingProvider.getBillingManager().querySkuDetailsAsync(BillingClient.SkuType.INAPP,
                inAppSkus,
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                FunctionsClassDebug.Companion.PrintDebug("*** SKU List ::: " + skuDetails + " ***");
                                if (skuDetails.getSku().equals("remove.ads") && functionsClass.removeAdsPurchased()) {
                                    itemDemoList.setVisibility(View.INVISIBLE);
                                    itemDemo.setVisibility(View.INVISIBLE);
                                    itemDemoDescription.setVisibility(View.INVISIBLE);

                                    continue;
                                }

                                if (skuDetails.getSku().equals("donation") && functionsClass.alreadyDonated()) {

                                    continue;
                                }

                                skuRowDataList.add(new SkuRowData(
                                        skuDetails,
                                        skuDetails.getSku(),
                                        skuDetails.getTitle(),
                                        skuDetails.getPrice(),
                                        skuDetails.getDescription(),
                                        skuDetails.getType())
                                );
                            }

                            if (skuRowDataList.size() == 0) {
                                displayError();
                            } else {
                                skusAdapter.updateData(skuRowDataList);
                            }

                            List<String> subsSkus = billingProvider.getBillingManager().getSkus(BillingClient.SkuType.SUBS);
                            billingProvider.getBillingManager().querySkuDetailsAsync(BillingClient.SkuType.SUBS,
                                    subsSkus,
                                    new SkuDetailsResponseListener() {
                                        @Override
                                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                                for (SkuDetails skuDetails : skuDetailsList) {
                                                    FunctionsClassDebug.Companion.PrintDebug("*** SKU List ::: " + skuDetails + " ***");
                                                    if (skuDetails.getSku().equals("remove.ads") && functionsClass.removeAdsPurchased()) {
                                                        itemDemoList.setVisibility(View.INVISIBLE);
                                                        itemDemo.setVisibility(View.INVISIBLE);
                                                        itemDemoDescription.setVisibility(View.INVISIBLE);

                                                        continue;
                                                    }

                                                    if (skuDetails.getSku().equals("donation") && functionsClass.alreadyDonated()) {

                                                        continue;
                                                    }

                                                    skuRowDataList.add(new SkuRowData(
                                                            skuDetails,
                                                            skuDetails.getSku(),
                                                            skuDetails.getTitle(),
                                                            skuDetails.getIntroductoryPrice(),
                                                            skuDetails.getDescription(),
                                                            skuDetails.getType())
                                                    );
                                                }
                                                if (skuRowDataList.size() == 0) {
                                                    displayError();
                                                } else {
                                                    skusAdapter.updateData(skuRowDataList);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void displayError() {
        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_LONG).show();
    }
}

