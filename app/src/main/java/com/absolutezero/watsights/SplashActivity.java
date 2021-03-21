package com.absolutezero.watsights;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_CANCELED;
import static android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_HW_NOT_PRESENT;
import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
import static androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
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
                    if (sharedPreferences.getBoolean("biometrics", false)) {
                        biometric();
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
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
    @RequiresApi(api = Build.VERSION_CODES.P)
    void biometric(){
        Executor newExecutor = Executors.newSingleThreadExecutor();
        FragmentActivity activity = this;
        BiometricPrompt myBiometricPrompt = new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {
            @Override
//onAuthenticationError is called when a fatal error occurrs//
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                switch (errorCode) {
                    case BIOMETRIC_ERROR_CANCELED:
                        Log.i(TAG, "Cancelled");
                        SplashActivity.this.finish();
                        SplashActivity.super.finish();
                        Log.i(TAG, "CANCELLED");
                        System.exit(0);
                        break;
                    case BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Log.i(TAG, "The operation was canceled because the biometric sensor is unavailable");
                        break;
                    case BIOMETRIC_ERROR_HW_NOT_PRESENT:
                        Log.i(TAG, "The device does not have a biometric sensor");
                        break;
                    // etc...
                }
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "Fingerprint recognised successfully");
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, "Fingerprint not recognised");
            }
        });
        final BiometricPrompt.PromptInfo promptInfo;
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Verification Required")
                    .setSubtitle("Place your finger on the fingerprint scanner to continue")
                    .setConfirmationRequired(true)
                    .setDeviceCredentialAllowed(true)
                    .build();
        myBiometricPrompt.authenticate(promptInfo);

    }
}