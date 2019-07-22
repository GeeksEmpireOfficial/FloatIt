package net.geekstools.floatshort.PRO;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.Functions.PublicVariable;

import java.util.ArrayList;

public class CheckPoint extends Activity {

    FunctionsClass functionsClass;

    RelativeLayout entry;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 666: {
                if (functionsClass.returnAPI() >= 26) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && Settings.canDrawOverlays(getApplicationContext())) {
                        functionsClass.savePreference(".Configuration", "Permissions", true);
                        startActivity(new Intent(getApplicationContext(), Configurations.class));
                        CheckPoint.this.finish();
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        setContentView(R.layout.check_point);
        entry = (RelativeLayout) findViewById(R.id.entry);

        functionsClass = new FunctionsClass(getApplicationContext(), CheckPoint.this);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (functionsClass.returnAPI() >= 26) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                window.setStatusBarColor(PublicVariable.themeColor);
                window.setNavigationBarColor(PublicVariable.themeColor);
            } else {
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        } else {
            window.setStatusBarColor(PublicVariable.themeColor);
            window.setNavigationBarColor(PublicVariable.themeColor);
        }

        if (getIntent().hasExtra(getString(R.string.splitIt))) {
            functionsClass.AccessibilityService(CheckPoint.this);
        } else {
            ArrayList<String> Permissions = new ArrayList<String>();
            Permissions.add(Manifest.permission.INTERNET);
            Permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
            Permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            Permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            Permissions.add(Manifest.permission.WAKE_LOCK);
            Permissions.add(Manifest.permission.BLUETOOTH);
            Permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
            Permissions.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
            Permissions.add(Manifest.permission.VIBRATE);
            if (functionsClass.returnAPI() >= 26) {
                Permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                Permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            requestPermissions(Permissions.toArray(new String[Permissions.size()]), 666);

            if (!Settings.canDrawOverlays(getApplicationContext())) {
                canOverlyPermission();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 666) {
            if (Settings.canDrawOverlays(getApplicationContext())) {
                FunctionsClass.println("*** Overlay Permission Granted ***");

                startActivity(new Intent(getApplicationContext(), Configurations.class),
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle());
            }
        }
    }

    public void canOverlyPermission() {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.GeeksEmpire_Dialogue_Light);
        alertDialog.setTitle(Html.fromHtml("<font color='" + PublicVariable.themeColor + "'>" +
                getResources().getString(R.string.permTitle) + "</font>"));
        alertDialog.setMessage(
                Html.fromHtml("<font color='" + PublicVariable.themeColorString + "'>" +
                        getResources().getString(R.string.permDesc) + "</font>"));
        alertDialog.setIcon(getDrawable(R.drawable.ic_launcher));
        alertDialog.setCancelable(true);

        alertDialog.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startService(new Intent(getApplicationContext(), BindServices.class));
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 666);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.overlayPermission), Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
