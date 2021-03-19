package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;

public class ViewSummaryActivity extends AppCompatActivity {
    private static final String TAG = "ViewSummaryActivity";
    Context context = this;
    DbHelper dbHelper;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summary);

    }
}