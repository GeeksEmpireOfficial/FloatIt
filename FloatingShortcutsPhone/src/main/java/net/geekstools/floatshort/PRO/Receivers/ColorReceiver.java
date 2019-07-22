package net.geekstools.floatshort.PRO.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class ColorReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context colorContext, Intent intent) {
        context = colorContext;
        FunctionsClass.println("Wallpaper Changed".toUpperCase());

        new ExtractColor().execute();
    }

    private class ExtractColor extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            FunctionsClass functionsClass = new FunctionsClass(context);
            functionsClass.extractWallpaperColor();
            return null;
        }
    }
}
