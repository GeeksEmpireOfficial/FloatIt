package net.geekstools.floatshort.PRO.Util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Shortcuts.ListViewOff;
import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;

public class SettingGUI extends WearableActivity {

    FunctionsClass functionsClass;
    RelativeLayout mContainerView;
    TextView theme, hideText, noneIcon, shape, splashTitle, remoteRecovery, bootText;
    ImageView dropletIcon, circleIcon, squareIcon, squircleIcon, cutCircleIcon;
    RadioButton dark, light;
    Switch hide, floatingSplash, bootReceiver;

    FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().isScreenRound()) {
            setContentView(R.layout.activity_setting_gui_round);
        } else {
            setContentView(R.layout.activity_setting_gui_rect);
        }
        setAmbientEnabled();
        functionsClass = new FunctionsClass(getApplicationContext(), this);

        mContainerView = (RelativeLayout) findViewById(R.id.container);
        theme = (TextView) findViewById(R.id.theme);
        hideText = (TextView) findViewById(R.id.hideText);
        splashTitle = (TextView) findViewById(R.id.floatingSplashText);
        shape = (TextView) findViewById(R.id.shape);
        noneIcon = (TextView) findViewById(R.id.noneIcon);
        remoteRecovery = (TextView) findViewById(R.id.remoteRecovery);
        bootText = (TextView) findViewById(R.id.bootText);
        dropletIcon = (ImageView) findViewById(R.id.dropletIcon);
        circleIcon = (ImageView) findViewById(R.id.circleIcon);
        squareIcon = (ImageView) findViewById(R.id.squareIcon);
        squircleIcon = (ImageView) findViewById(R.id.squircleIcon);
        cutCircleIcon = (ImageView) findViewById(R.id.cutCircleIcon);
        dark = (RadioButton) findViewById(R.id.dark);
        light = (RadioButton) findViewById(R.id.light);
        hide = (Switch) findViewById(R.id.hide);
        floatingSplash = (Switch) findViewById(R.id.floatingSplash);
        bootReceiver = (Switch) findViewById(R.id.bootReceiver);

        final SharedPreferences sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("themeColor", false) == true) {
            light.setChecked(true);
            mContainerView.setBackgroundColor(getColor(R.color.light_trans));
            dark.setTextColor(getColor(R.color.dark));
            light.setTextColor(getColor(R.color.dark));
            theme.setTextColor(getColor(R.color.dark));
            hideText.setTextColor(getColor(R.color.dark));
            hide.setTextColor(getColor(R.color.dark));
            shape.setTextColor(getColor(R.color.dark));
            floatingSplash.setTextColor(getColor(R.color.dark));
            splashTitle.setTextColor(getColor(R.color.dark));
            remoteRecovery.setTextColor(getColor(R.color.dark));
            bootReceiver.setTextColor(getColor(R.color.dark));
            bootText.setTextColor(getColor(R.color.dark));

            dark.setChecked(false);
        } else if (sharedPreferences.getBoolean("themeColor", false) == false) {
            dark.setChecked(true);
            mContainerView.setBackgroundColor(getColor(R.color.trans_black));
            dark.setTextColor(getColor(R.color.light));
            light.setTextColor(getColor(R.color.light));
            theme.setTextColor(getColor(R.color.light));
            hideText.setTextColor(getColor(R.color.light));
            hide.setTextColor(getColor(R.color.light));
            shape.setTextColor(getColor(R.color.light));
            floatingSplash.setTextColor(getColor(R.color.light));
            splashTitle.setTextColor(getColor(R.color.light));
            remoteRecovery.setTextColor(getColor(R.color.light));
            bootReceiver.setTextColor(getColor(R.color.light));
            bootText.setTextColor(getColor(R.color.light));

            light.setChecked(false);
        }

        final Boolean trans = sharedPreferences.getBoolean("hide", false);
        if (trans == true) {
            hide.setChecked(true);
        } else if (trans == false) {
            hide.setChecked(false);
        }

        if (functionsClass.splashReveal()) {
            floatingSplash.setChecked(true);
        } else {
            floatingSplash.setChecked(false);
        }

        if (functionsClass.bootReceiverEnabled()) {
            bootReceiver.setChecked(true);
        } else {
            bootReceiver.setChecked(false);
        }

        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("themeColor", false);
                editor.apply();

                dark.setChecked(true);
                light.setChecked(false);

                mContainerView.setBackgroundColor(getColor(R.color.trans_black));
                dark.setTextColor(getColor(R.color.light));
                light.setTextColor(getColor(R.color.light));
                theme.setTextColor(getColor(R.color.light));
                hideText.setTextColor(getColor(R.color.light));
                hide.setTextColor(getColor(R.color.light));
                shape.setTextColor(getColor(R.color.light));
                floatingSplash.setTextColor(getColor(R.color.light));
                splashTitle.setTextColor(getColor(R.color.light));
                remoteRecovery.setTextColor(getColor(R.color.light));
                bootReceiver.setTextColor(getColor(R.color.light));
                bootText.setTextColor(getColor(R.color.light));
            }
        });
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("themeColor", true);
                editor.apply();

                light.setChecked(true);
                dark.setChecked(false);

                mContainerView.setBackgroundColor(getColor(R.color.light_trans));
                dark.setTextColor(getColor(R.color.dark));
                light.setTextColor(getColor(R.color.dark));
                theme.setTextColor(getColor(R.color.dark));
                hideText.setTextColor(getColor(R.color.dark));
                hide.setTextColor(getColor(R.color.dark));
                shape.setTextColor(getColor(R.color.dark));
                floatingSplash.setTextColor(getColor(R.color.dark));
                splashTitle.setTextColor(getColor(R.color.dark));
                remoteRecovery.setTextColor(getColor(R.color.dark));
                bootReceiver.setTextColor(getColor(R.color.dark));
                bootText.setTextColor(getColor(R.color.dark));
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trans == true) {
                    editor.putBoolean("hide", false);
                    editor.apply();
                } else if (trans == false) {
                    editor.putBoolean("hide", true);
                    editor.apply();
                }
            }
        });

        floatingSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (functionsClass.splashReveal()) {
                    editor.putBoolean("floatingSplash", false);
                    editor.apply();
                } else if (!functionsClass.splashReveal()) {
                    editor.putBoolean("floatingSplash", true);
                    editor.apply();
                }
            }
        });

     /*   bootReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(functionsClass.bootReceiverEnabled()){
                    functionsClass.savePreference("SmartFeature", "remoteRecovery", false);
                }
                else if(!functionsClass.bootReceiverEnabled()){
                    functionsClass.savePreference("SmartFeature", "remoteRecovery", true);
                }
            }
        });*/

        bootReceiver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                functionsClass.savePreference("SmartFeature", "remoteRecovery", isChecked);

            }
        });

        /*switch to enable select icon*/
        switch (sharedPreferences.getInt("iconShape", 0)) {
            case 1:
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_light));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                break;
            case 2:
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_light));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                break;
            case 3:
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_light));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                break;
            case 4:
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_light));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                break;
            case 5:
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_light));
                break;
            case 0:
                noneIcon.setTextColor(getColor(R.color.default_color_light));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                break;
        }

        /*clickListeners to select shape*/
        noneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("iconShape", 0);
                editor.apply();
                noneIcon.setTextColor(getColor(R.color.default_color_light));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
            }
        });
        dropletIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("iconShape", 1);
                editor.apply();
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_light));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
            }
        });
        circleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("iconShape", 2);
                editor.apply();
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_light));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
            }
        });
        squareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("iconShape", 3);
                editor.apply();
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_light));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
            }
        });
        squircleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("iconShape", 4);
                editor.apply();
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_light));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_darker));
            }
        });
        cutCircleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("iconShape", 5);
                editor.apply();
                noneIcon.setTextColor(getColor(R.color.default_color_darker));
                dropletIcon.setColorFilter(getColor(R.color.default_color_darker));
                circleIcon.setColorFilter(getColor(R.color.default_color_darker));
                squareIcon.setColorFilter(getColor(R.color.default_color_darker));
                squircleIcon.setColorFilter(getColor(R.color.default_color_darker));
                cutCircleIcon.setColorFilter(getColor(R.color.default_color_light));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Button back = (Button) findViewById(R.id.back);
        Button support = (Button) findViewById(R.id.support);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListViewOff.class));
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMsg = "\n\n\n\n\n"
                        + "[Essential Information]" + "\n"
                        + functionsClass.getDeviceName() + " | " + "API " + Build.VERSION.SDK_INT + " | " + functionsClass.getCountryIso().toUpperCase();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.support)});
                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_tag) + " [" + functionsClass.appVersion(getPackageName()) + "] ");
                email.putExtra(Intent.EXTRA_TEXT, textMsg);
                //email.setType("text/*");
                email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(email, getString(R.string.feedback_tag)));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default);
        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(SettingGUI.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                            if (firebaseRemoteConfig.getLong(functionsClass.versionCodeRemoteConfigKey()) > functionsClass.appVersionCode(getPackageName())) {
                                Toast.makeText(getApplicationContext(), getString(R.string.updateAvailable), Toast.LENGTH_LONG).show();
                            } else {
                            }
                        } else {
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
        } else {
        }
    }
}
