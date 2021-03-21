package com.absolutezero.watsights;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.absolutezero.watsights.Adapters.ImportantMessageAdapter;
import com.absolutezero.watsights.Adapters.PinnedMessageAdapter;
import com.absolutezero.watsights.Models.Pinned;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ViewPinnedMessagesActivity extends AppCompatActivity {
    private static final String TAG = "ViewPinnedMessagesActiv";
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Pinned> arrayList;
    DbHelper dbHelper;
    Context context = this;
    PinnedMessageAdapter messageAdapter;
    TextView count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        count = findViewById(R.id.count);
        dbHelper = new DbHelper(context);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pinned Messages");
        arrayList = dbHelper.getPinnedMessages();
        count.setText(arrayList.size() + " messages pinned");
        if (arrayList.size() == 0) {
            findViewById(R.id.notFound).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            messageAdapter = new PinnedMessageAdapter(context, arrayList);
            recyclerView.setAdapter(messageAdapter);
            recyclerView.scrollToPosition(arrayList.size() - 1);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Snackbar snackbar = Snackbar.make(context, view, "Long press on a message to unpin it", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                }

                @Override
                public void onLongClick(View view, int position) {
                    Log.d(TAG, "onLongClick() returned: " + arrayList.get(position).getId());
                    long messageId = arrayList.get(position).getMessageId();
                    if (dbHelper.removePinned(arrayList.get(position).getId())) {
                        arrayList.remove(position);
                        messageAdapter.notifyItemRemoved(position);
                        count.setText(arrayList.size() + " messages pinned");
                        Snackbar snackbar = Snackbar.make(context, view, "Message Unpinned", BaseTransientBottomBar.LENGTH_SHORT)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        long id = dbHelper.addPinned(messageId);
                                        if (id != -1) {
                                            Toast.makeText(context, "Message Pinned", Toast.LENGTH_SHORT).show();
                                            arrayList.add(position - 1, dbHelper.getPinnedMessage(id));
                                            messageAdapter.notifyItemInserted(position - 1);
                                            count.setText(arrayList.size() + " messages pinned");
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_group_chat, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_info:

                break;
            default:
                Toast.makeText(context, "Under Construction", Toast.LENGTH_SHORT).show();

        }
        return false;
    }
}