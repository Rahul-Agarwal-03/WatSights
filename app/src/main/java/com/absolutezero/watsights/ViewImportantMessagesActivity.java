package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.absolutezero.watsights.Models.Important;

import java.util.ArrayList;

public class ViewImportantMessagesActivity extends AppCompatActivity {
    private static final String TAG = "ViewImportantMessagesAc";
    Context context = this;
    DbHelper dbHelper;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Important> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_important_messages);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        dbHelper = new DbHelper(context);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        arrayList = dbHelper.getImportantMessages();
    }
}