package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkPermissionOrStart();
    }
    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),"enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name: names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionOrStart();
    }
    void checkPermissionOrStart(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isNotificationServiceEnabled()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    new AlertDialog.Builder(SplashActivity.this)
                            .setTitle("Enable Notification Access")
                            .setMessage("Please allow " + getString(R.string.app_name) + " to read notifications.\nThese messages are stored locally and are not accessible by us")
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                                }
                            }).show();
                }
            }
        },1000);
    }
}