package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.absolutezero.watsights.Adapters.PersonAdapter;
import com.absolutezero.watsights.Models.Person;

import java.util.ArrayList;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView1, recyclerView2;
    PersonAdapter personAdapter1, personAdapter2;
    ArrayList<Person> elite, spammers;
    DbHelper dbHelper;
    Context context = this;
    Toolbar toolbar;
    CardView c1, c2, c3;
    RadioButton r1, r2, r3;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Switch biometrics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        dbHelper = new DbHelper(this);
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        elite = dbHelper.getElitePeople();
        spammers = dbHelper.getSpammers();
        personAdapter1 = new PersonAdapter(context, elite);
        personAdapter2 = new PersonAdapter(context, spammers);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
        recyclerView1.setAdapter(personAdapter1);
        recyclerView2.setAdapter(personAdapter2);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        biometrics = findViewById(R.id.biometrics);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        biometrics.setChecked(sharedPreferences.getBoolean("biometrics", false));
        biometrics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("biometrics", isChecked);
                editor.apply();
            }
        });
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            setRadio(1);
        }else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setRadio(2);
        }
        else{
            setRadio(3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.c1:
            case R.id.r1:
                setRadio(1);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case R.id.c2:
            case R.id.r2:
                setRadio(2);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;

            case R.id.c3:
            case R.id.r3:
                setRadio(3);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    void setRadio(int n){
        switch(n){
            case 1:
                r1.setChecked(true);
                r2.setChecked(false);
                r3.setChecked(false);
                break;
            case 2:
                r1.setChecked(false);
                r2.setChecked(true);
                r3.setChecked(false);
                break;
            case 3:
                r1.setChecked(false);
                r2.setChecked(false);
                r3.setChecked(true);
                break;
        }
    }

    public void resetImportant(View view) {
        new AlertDialog.Builder(context)
        .setTitle("Confirm Reset")
        .setMessage("Are you sure you want to remove all the important members?\nThis action cannot be undone")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dbHelper.resetElite()) {
                    Toast.makeText(context, "All Important Members removed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(context, "Couldnt't reset. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
    public void resetSpammers(View view) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm Reset")
                .setMessage("Are you sure you want to remove all the spammers members?\nThis action cannot be undone")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dbHelper.resetSpam()) {
                            Toast.makeText(context, "All Spammers removed", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(context, "Couldnt't reset. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}