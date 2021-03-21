package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.absolutezero.watsights.Adapters.ImportantMessageAdapter;
import com.absolutezero.watsights.Adapters.PersonAdapter;
import com.absolutezero.watsights.Models.Person;

import java.util.ArrayList;

public class ViewElitePeopleActivity extends AppCompatActivity {
    private static final String TAG = "ViewImportantPeopleActi";
    ArrayList<Person> arrayList;
    DbHelper dbHelper = new DbHelper(this);
    Context context = this;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Elite Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        count = findViewById(R.id.count);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        arrayList = dbHelper.getElitePeople();
        if (arrayList.size() == 0) {
            findViewById(R.id.notFound).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            PersonAdapter personAdapter = new PersonAdapter(context, arrayList);
            recyclerView.setAdapter(personAdapter);
            count.setText(arrayList.size()+" groups");
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(context, ViewEliteMessagesActivity.class);
                    Log.d(TAG, "onLongClick() returned: " + arrayList.get(position).getId());
                    Bundle bundle = new Bundle();
                    bundle.putLong("person_id",arrayList.get(position).getId());
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
}