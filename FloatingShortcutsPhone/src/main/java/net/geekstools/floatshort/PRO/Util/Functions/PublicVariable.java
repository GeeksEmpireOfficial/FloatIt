package net.geekstools.floatshort.PRO.Util.Functions;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PublicVariable {

    public static Activity activityStatic;
    public static Context contextStatic;

    public static int appInfoCount = 0;
    public static int floatingCounter = 0;
    public static int shortcutsCounter = -1;

    public static int floatingCategoryCounter_category = 0;
    public static int floatingCategoryCounter_wifi = 0;
    public static int floatingCategoryCounter_bluetooth = 0;
    public static int floatingCategoryCounter_gps = 0;
    public static int floatingCategoryCounter_nfc = 0;
    public static int floatingCategoryCounter_time = 0;
    public static int categoriesCounter = -1;
    public static int floatingWidgetsCounter_Widgets = 0;
    public static int widgetsCounter = -1;
    public static int HW = 0;
    public static int size;
    public static int actionBarHeight;
    public static int statusBarHeight;
    public static int navigationBarHeight;
    public static int primaryColor;
    public static int primaryColorOpposite;
    public static int colorLightDark;
    public static int colorLightDarkOpposite;
    public static int dominantColor;
    public static int themeColor;
    public static int themeTextColor;
    public static int itemPosition;
    public static int freqLength;

    public static float confirmButtonX;
    public static float confirmButtonY;

    public static String themeColorString;
    public static String autoID = null;
    public static String categoryName = "FloatingCategory";
    public static String splitPairPackage;
    public static String splitSinglePackage;
    public static String splitSingleClassName;
    public static String previousDuplicated;
    public static final String WIDGET_DATA_DATABASE_NAME = "WidgetData";

    public static String[] freqApps;

    public static boolean inMemory = false;
    public static boolean hearBeatCheckPoint = false;
    public static boolean themeLightDark = true;
    public static boolean Stable = false;
    public static boolean actionCenter = false;
    public static boolean recoveryCenter = false;
    public static boolean receiveWiFi = false;
    public static boolean receiveBluetooth = false;
    public static boolean receiverGPS = false;
    public static boolean receiverNFC = false;
    public static boolean splitScreen = true;
    public static boolean triggerWifiBroadcast = false;
    public static boolean forceReload = false;

    public static ArrayList<String> FloatingShortcuts = new ArrayList<String>();
    public static ArrayList<String> RecoveryShortcuts = new ArrayList<String>();
    public static ArrayList<String> FloatingCategories = new ArrayList<String>();
    public static ArrayList<Integer> FloatingWidgets = new ArrayList<Integer>();

    public static List<String> customIconsPackages = new ArrayList<String>();

    public static Map<String, PendingIntent> notificationIntent = new LinkedHashMap<String, PendingIntent>();

    /*FREE*/
    public static String targetClassAfterAds = null;
    public static boolean interstitialAdLoaded = false;
    public static boolean AdsRewarded = false;
    public static boolean eligibleShowAds = false;
    /*FREE*/
}
