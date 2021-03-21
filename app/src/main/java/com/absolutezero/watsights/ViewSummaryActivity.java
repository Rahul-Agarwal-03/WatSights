package com.absolutezero.watsights;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ViewSummaryActivity extends AppCompatActivity {
    private static final String TAG = "ViewSummaryActivity";
    Context context = this;
    DbHelper dbHelper;
    Toolbar toolbar;
    long group_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summary);
        dbHelper = new DbHelper(context);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Message Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        group_id = getIntent().getBundleExtra("bundle").getLong("group_id");
        getSupportActionBar().setSubtitle(dbHelper.getGroup(group_id).getName());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                Toast.makeText(context, "Under Construction", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}