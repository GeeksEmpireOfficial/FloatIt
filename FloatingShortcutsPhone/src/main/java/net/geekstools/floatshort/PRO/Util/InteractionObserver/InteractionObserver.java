package net.geekstools.floatshort.PRO.Util.InteractionObserver;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.accessibility.AccessibilityEvent;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class InteractionObserver extends AccessibilityService {

    FunctionsClass functionsClass;

    boolean doSplitPair = false;
    boolean doSplitSingle = false;

    String classNameCommand = "Default";

    @Override
    protected void onServiceConnected() {
        functionsClass = new FunctionsClass(getApplicationContext());
    }

    @Override
    public void onAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        functionsClass = new FunctionsClass(getApplicationContext());

        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (accessibilityEvent.getAction() == 10296) {
                    classNameCommand = (String) accessibilityEvent.getClassName();

                    if (doSplitPair == false) {
                        doSplitPair = true;
                        startActivity(new Intent(getApplicationContext(), SplitTransparentPair.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }

                    IntentFilter intentFilterTest = new IntentFilter();
                    intentFilterTest.addAction("perform_split_pair");
                    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (intent.getAction().equals("perform_split_pair") && doSplitPair == true) {
                                doSplitPair = false;
                                performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);
                                sendBroadcast(new Intent("Split_Apps_Pair_" + classNameCommand));
                            }
                        }
                    };
                    registerReceiver(broadcastReceiver, intentFilterTest);
                } else if (accessibilityEvent.getAction() == 69201) {
                    classNameCommand = (String) accessibilityEvent.getClassName();

                    if (doSplitSingle == false) {
                        doSplitSingle = true;
                        startActivity(new Intent(getApplicationContext(), SplitTransparentSingle.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }

                    IntentFilter intentFilterTest = new IntentFilter();
                    intentFilterTest.addAction("perform_split_single");
                    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (intent.getAction().equals("perform_split_single") && doSplitSingle == true) {
                                doSplitSingle = false;
                                performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);
                                sendBroadcast(new Intent("Split_Apps_Single_" + classNameCommand));
                            }
                        }
                    };
                    registerReceiver(broadcastReceiver, intentFilterTest);
                } else if (accessibilityEvent.getAction() == 66666) {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
                }
                break;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        return START_STICKY;
    }

    @Override
    public void onInterrupt() {
        startService(new Intent(getApplicationContext(), InteractionObserver.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
