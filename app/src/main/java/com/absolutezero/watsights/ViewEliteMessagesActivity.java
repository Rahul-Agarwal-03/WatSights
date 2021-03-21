package com.absolutezero.watsights;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.absolutezero.watsights.Adapters.EliteMessageAdapter;
import com.absolutezero.watsights.Adapters.MessageAdapter;
import com.absolutezero.watsights.Models.Message;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ViewEliteMessagesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Message> arrayList;
    DbHelper dbHelper;
    Context context = this;
    long person_id;
    EliteMessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chats);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        dbHelper = new DbHelper(context);
        person_id = getIntent().getBundleExtra("bundle").getLong("person_id");
        if (person_id == -1) {
            Toast.makeText(context, "Unable to fetch group details", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(dbHelper.getPerson(person_id).getName());
            arrayList = dbHelper.getPersonMessages(person_id);
            if (arrayList.size() == 0) {
                findViewById(R.id.notFound).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else {
                findViewById(R.id.notFound).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                messageAdapter = new EliteMessageAdapter(context, arrayList);
                recyclerView.setAdapter(messageAdapter);
                recyclerView.scrollToPosition(arrayList.size() - 1);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_group_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_info:
                Intent intent = new Intent(context, ViewGroupInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("person_id",person_id);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;
            default:
                Toast.makeText(context, "Under Construction", Toast.LENGTH_SHORT).show();

        }
        return false;
    }
}