package net.geekstools.floatshort.PRO.Util.IAP;

import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.FragmentActivity;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;
import net.geekstools.floatshort.PRO.Util.IAP.billing.BillingManager;
import net.geekstools.floatshort.PRO.Util.IAP.billing.BillingProvider;

public class InAppBilling extends FragmentActivity implements BillingProvider {

    private static final String TAG = "InAppBilling";
    private static final String DIALOG_TAG = "InAppBillingDialogue";

    FunctionsClass functionsClass;

    private BillingManager billingManager;
    private AcquireFragment acquireFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PublicVariable.themeLightDark) {
            setTheme(R.style.GeeksEmpire_Material_IAP_LIGHT);
        } else {
            setTheme(R.style.GeeksEmpire_Material_IAP_DARK);
        }

        functionsClass = new FunctionsClass(getApplicationContext(), InAppBilling.this);

        if (savedInstanceState != null) {
            acquireFragment = (AcquireFragment) getFragmentManager().findFragmentByTag(DIALOG_TAG);
        }

        billingManager = new BillingManager(InAppBilling.this, getIntent().hasExtra("UserEmailAddress") ? getIntent().getStringExtra("UserEmailAddress") : null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                proceedToPurchaseFragment();
            }
        }, 777);

        showRefreshedUi();
    }

    @Override
    public BillingManager getBillingManager() {
        return billingManager;
    }

    public void proceedToPurchaseFragment() {
        if (acquireFragment == null) {
            acquireFragment = new AcquireFragment();
        }

        if (!isAcquireFragmentShown()) {
            acquireFragment.show(getFragmentManager(), DIALOG_TAG);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    public void showRefreshedUi() {
        if (isAcquireFragmentShown()) {
            acquireFragment.refreshUI();
        }
    }

    public boolean isAcquireFragmentShown() {
        return acquireFragment != null && acquireFragment.isVisible();
    }
}
