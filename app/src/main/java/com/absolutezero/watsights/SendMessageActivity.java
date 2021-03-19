package com.absolutezero.watsights;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class SendMessageActivity extends AppCompatActivity {
    Context context = this;
    TextInputEditText mobNo, message;
    TextInputLayout mobNo1, message1;
    Toolbar toolbar;
    CountryCodePicker countryCodePicker;
    private static final String TAG = "SendMessageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mobNo = findViewById(R.id.mobNo);
        mobNo1 = findViewById(R.id.mobNo1);
        message = findViewById(R.id.message);
        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerPhoneNumberTextView(mobNo);
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = countryCodePicker.getFullNumberWithPlus();
                Log.d(TAG, "onClick() returned: " + phone);
                openWhatsApp(phone, (message.getText().toString().length() != 0) ? message.getText().toString() : null);
            }
        });
        findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openWhatsApp(String phone, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (text != null) {
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + phone + "&text=" + text));
            }
            else{
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + phone));
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
