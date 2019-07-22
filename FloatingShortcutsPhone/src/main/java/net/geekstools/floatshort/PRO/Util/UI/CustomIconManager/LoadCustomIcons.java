package net.geekstools.floatshort.PRO.Util.UI.CustomIconManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LoadCustomIcons {

    public Context context;
    public String packageNameIconPack;
    public String name;
    public boolean iconsLoaded = false;
    public HashMap<String, String> mapPackagesDrawables = new HashMap<String, String>();
    public List<Bitmap> mBackImages = new ArrayList<Bitmap>();
    public Bitmap mMaskImage = null;
    public Bitmap mFrontImage = null;
    public Bitmap backIconMask = null;
    public float mFactor = 1.0f;
    public int totalIcons = 0;
    FunctionsClass functionsClass;
    Resources iconPackres = null;

    public LoadCustomIcons(Context context, String iconsPackageName) {
        this.context = context;
        this.packageNameIconPack = iconsPackageName;

        functionsClass = new FunctionsClass(context);
    }

    public void load() {
        if (iconsLoaded) {
            return;
        }
        // load appfilter.xml from the icon pack package
        PackageManager packageManager = context.getPackageManager();
        try {
            XmlPullParser xpp = null;

            iconPackres = packageManager.getResourcesForApplication(packageNameIconPack);
            int appfilterid = iconPackres.getIdentifier("appfilter", "xml", packageNameIconPack);
            if (appfilterid > 0) {
                xpp = iconPackres.getXml(appfilterid);
            } else {
                // no resource found, try to open it from assests folder
                try {
                    InputStream appfilterstream = iconPackres.getAssets().open("appfilter.xml");

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    xpp = factory.newPullParser();
                    xpp.setInput(appfilterstream, "utf-8");
                } catch (IOException e1) {
                    //Ln.d("No appfilter.xml file");
                }
            }

            if (xpp != null) {
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("iconback")) {
                            try {
                                backIconMask = loadBitmap(xpp.getAttributeValue(0));
                            } catch (Exception e) {
                                e.printStackTrace();
                                backIconMask = null;
                            }
                            for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                if (xpp.getAttributeName(i).startsWith("img")) {
                                    String drawableName = xpp.getAttributeValue(i);
                                    Bitmap iconback = loadBitmap(drawableName);
                                    if (iconback != null) {
                                        mBackImages.add(iconback);
                                    }
                                }
                            }
                        } else if (xpp.getName().equals("iconmask")) {
                            if (xpp.getAttributeCount() > 0 && xpp.getAttributeName(0).equals("img1")) {
                                String drawableName = xpp.getAttributeValue(0);
                                mMaskImage = loadBitmap(drawableName);
                            }
                        } else if (xpp.getName().equals("iconupon")) {
                            if (xpp.getAttributeCount() > 0 && xpp.getAttributeName(0).equals("img1")) {
                                String drawableName = xpp.getAttributeValue(0);
                                mFrontImage = loadBitmap(drawableName);
                            }
                        } else if (xpp.getName().equals("scale")) {
                            // mFactor
                            if (xpp.getAttributeCount() > 0 && xpp.getAttributeName(0).equals("factor")) {
                                mFactor = Float.valueOf(xpp.getAttributeValue(0));
                            }
                        } else if (xpp.getName().equals("item")) {
                            String componentName = null;
                            String drawableName = null;

                            for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                if (xpp.getAttributeName(i).equals("component")) {
                                    componentName = xpp.getAttributeValue(i);
                                } else if (xpp.getAttributeName(i).equals("drawable")) {
                                    drawableName = xpp.getAttributeValue(i);
                                }
                            }
                            if (!mapPackagesDrawables.containsKey(componentName)) {
                                mapPackagesDrawables.put(componentName, drawableName);
                            }
                        }
                    }
                    eventType = xpp.next();
                }
                this.totalIcons = mapPackagesDrawables.size();
            }
            iconsLoaded = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadBitmap(String drawableName) {
        int id = iconPackres.getIdentifier(drawableName, "drawable", packageNameIconPack);
        if (id > 0) {
            Drawable bitmap = iconPackres.getDrawable(id, iconPackres.newTheme());
            if (bitmap instanceof BitmapDrawable) {
                return ((BitmapDrawable) bitmap).getBitmap();
            } else {
                return functionsClass.drawableToBitmap(bitmap);
            }
        }
        return null;
    }

    private Bitmap loadBitmap(String appPackageName, String drawableName) {
        int id = iconPackres.getIdentifier(drawableName, "drawable", packageNameIconPack);
        if (id > 0) {
            Drawable bitmap = iconPackres.getDrawable(id, iconPackres.newTheme());
            if (bitmap instanceof BitmapDrawable) {
                return ((BitmapDrawable) bitmap).getBitmap();
            } else {
                return functionsClass.drawableToBitmap(bitmap);
            }
        } else {
            try {
                Drawable iconback = functionsClass.bitmapToDrawable(backIconMask);
                Drawable appIcon = functionsClass.appIcon(appPackageName);
                LayerDrawable layerDrawableIcon = new LayerDrawable(new Drawable[]{
                        iconback,
                        appIcon
                });
                layerDrawableIcon.setLayerInset(1, 77, 77, 77, 77);

                return functionsClass.drawableToBitmap(layerDrawableIcon);
            } catch (Exception e) {
                e.printStackTrace();

                return functionsClass.drawableToBitmap(functionsClass.shapedAppIcon(appPackageName));
            }
        }
    }

    private Drawable loadDrawable(String appPackageName, String drawableName) {
        int id = iconPackres.getIdentifier(drawableName, "drawable", packageNameIconPack);
        if (id > 0) {
            Drawable bitmap = iconPackres.getDrawable(id, iconPackres.newTheme());
            return bitmap;
        } else {
            try {
                Drawable iconback = functionsClass.bitmapToDrawable(backIconMask);
                Drawable appIcon = functionsClass.appIcon(appPackageName);
                LayerDrawable layerDrawableIcon = new LayerDrawable(new Drawable[]{
                        iconback,
                        appIcon
                });
                layerDrawableIcon.setLayerInset(1, 77, 77, 77, 77);

                return layerDrawableIcon;
            } catch (Exception e) {
                e.printStackTrace();

                return functionsClass.shapedAppIcon(appPackageName);
            }
        }
    }

    public Drawable getDrawableIconForPackage(String appPackageName, Drawable defaultDrawable) {
        if (!iconsLoaded) {
            load();
        }
        try {
            PackageManager pm = context.getPackageManager();

            Intent launchIntent = pm.getLaunchIntentForPackage(appPackageName);

            String componentName = null;

            if (launchIntent != null) {
                componentName = pm.getLaunchIntentForPackage(appPackageName).getComponent().toString();
            }

            String drawable = mapPackagesDrawables.get(componentName);

            if (drawable != null) {
                return loadDrawable(appPackageName, drawable);
            } else {
                // try to get a resource with the component filename
                if (componentName != null) {
                    int start = componentName.indexOf("{") + 1;
                    int end = componentName.indexOf("}", start);
                    if (end > start) {
                        drawable = componentName.substring(start, end).toLowerCase(Locale.getDefault()).replace(".", "_").replace("/", "_");
                        if (iconPackres.getIdentifier(drawable, "drawable", packageNameIconPack) > 0)
                            return loadDrawable(appPackageName, drawable);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultDrawable;
    }

    public Bitmap getIconForPackage(String appPackageName, Bitmap defaultBitmap) {
        if (!iconsLoaded) {
            load();
        }
        try {
            PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage(appPackageName);
            String componentName = null;
            if (launchIntent != null)
                componentName = pm.getLaunchIntentForPackage(appPackageName).getComponent().toString();
            String drawable = mapPackagesDrawables.get(componentName);
            if (drawable != null) {
                Bitmap BMP = loadBitmap(appPackageName, drawable);
                if (BMP == null) {
                    return defaultBitmap;
                } else {
                    return BMP;
                }
            } else {
                // try to get a resource with the component filename
                if (componentName != null) {
                    int start = componentName.indexOf("{") + 1;
                    int end = componentName.indexOf("}", start);
                    if (end > start) {
                        drawable = componentName.substring(start, end).toLowerCase(Locale.getDefault()).replace(".", "_").replace("/", "_");
                        if (iconPackres.getIdentifier(drawable, "drawable", packageNameIconPack) > 0)
                            return loadBitmap(appPackageName, drawable);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultBitmap;
    }

    public int getTotalIcons() {
        return this.totalIcons;
    }
}
