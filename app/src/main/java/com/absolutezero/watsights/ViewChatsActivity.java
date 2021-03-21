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

import com.absolutezero.watsights.Adapters.ImportantMessageAdapter;
import com.absolutezero.watsights.Adapters.MessageAdapter;
import com.absolutezero.watsights.Models.Message;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ViewChatsActivity extends AppCompatActivity {
    private static final String TAG = "ViewChatsActivity";
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Message> arrayList;
    DbHelper dbHelper;
    Context context = this;
    long group_id;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chats);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        dbHelper = new DbHelper(context);
        group_id = getIntent().getBundleExtra("bundle").getLong("group_id");
        findViewById(R.id.summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("group_id", group_id);
                Intent intent = new Intent(context, ViewSummaryActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
        if (group_id == -1) {
            Toast.makeText(context, "Unable to fetch group details", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(dbHelper.getGroup(group_id).getName());
            arrayList = dbHelper.getGroupMessages(group_id);
            if (arrayList.size() == 0) {
                findViewById(R.id.notFound).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else {
                findViewById(R.id.notFound).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                messageAdapter = new MessageAdapter(context, arrayList);
                recyclerView.setAdapter(messageAdapter);
                recyclerView.scrollToPosition(arrayList.size() - 1);
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Snackbar snackbar = Snackbar.make(context, view, "Long press on a message to pin it", BaseTransientBottomBar.LENGTH_SHORT);
                        snackbar.show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Log.d(TAG, "onLongClick() returned: " + arrayList.get(position).getId());
                        long id = dbHelper.addPinned(arrayList.get(position).getId());
                        if (id != -1) {
                            Snackbar snackbar = Snackbar.make(context, view, "Message Pinned", BaseTransientBottomBar.LENGTH_SHORT)
                                    .setAction("Undo", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (dbHelper.removePinned(id)) {
                                                Toast.makeText(context, "Message Unpinned", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                            snackbar.show();
                        }
                        else{
                            Toast.makeText(context, "Couldn't Pin Message", Toast.LENGTH_SHORT).show();
                        }

                    }
                }));
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
                bundle.putLong("group_id",group_id);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;
            default:
                Toast.makeText(context, "Under Construction", Toast.LENGTH_SHORT).show();

        }
        return false;
    }
}