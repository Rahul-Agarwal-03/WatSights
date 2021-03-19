package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.absolutezero.watsights.Adapters.GroupAdapter;
import com.absolutezero.watsights.Models.Group;

import java.util.ArrayList;

public class ViewGroupsActivity extends AppCompatActivity {
    Context context = this;
    Toolbar toolbar;
    RecyclerView recyclerView;
    DbHelper dbHelper;
    private static final String TAG = "ViewGroupsActivity";
    ArrayList<Group> arrayList;
    GroupAdapter groupAdapter;
    TextView count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);
        dbHelper = new DbHelper(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        recyclerView = findViewById(R.id.recyclerView);
        count = findViewById(R.id.count);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        arrayList = dbHelper.getGroups();
        groupAdapter = new GroupAdapter(context, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(groupAdapter);
        count.setText(arrayList.size()+" groups");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, ViewChatsActivity.class);
                Log.d(TAG, "onLongClick() returned: " + arrayList.get(position).getId());
                Bundle bundle = new Bundle();
                bundle.putLong("group_id",arrayList.get(position).getId());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.d(TAG, "onLongClick() returned: " + arrayList.get(position).getId());
            }
        }));
    }
}