package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.absolutezero.watsights.Adapters.MemberAdapter;
import com.absolutezero.watsights.Models.Group;
import com.absolutezero.watsights.Models.Member;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewGroupInfoActivity extends AppCompatActivity {
    private static final String TAG = "ViewGroupInfoActivity";
    RecyclerView recyclerView;
    Toolbar toolbar;
    long group_id;
    DbHelper dbHelper;
    Context context = this;
    Switch store_messages;
    TextView priority, name;
    SeekBar seekBar;
    Group group;
    CircleImageView icon;
    ArrayList<Member> arrayList;
    MemberAdapter memberAdapter;
    String options[] = {"Elite", "Spammer","None"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_info);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        dbHelper = new DbHelper(context);
        group_id = getIntent().getBundleExtra("bundle").getLong("group_id");
        name = findViewById(R.id.name);
        icon = findViewById(R.id.icon);
        seekBar = findViewById(R.id.seekBar);
        priority = findViewById(R.id.priority);
        store_messages = findViewById(R.id.switch_store_messages);
        if (group_id == -1) {
            Toast.makeText(context, "Unable to fetch group details", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            group = dbHelper.getGroup(group_id);
            name.setText(group.getName());
            icon.setCircleBackgroundColor(group.getIcon());
            arrayList = dbHelper.getMembers(group_id);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            memberAdapter = new MemberAdapter(context, arrayList);
            recyclerView.setAdapter(memberAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    long memberId = arrayList.get(position).getId();
                    Log.d(TAG, "onClick() returned: memberid" + memberId);
                    Member member = dbHelper.getMember(memberId);
                    long personId = member.getPersonId();
                    Log.d(TAG, "onLongClick() returned: personId" + personId);
                    int checkedItem = dbHelper.isImportant(personId) ? 0 : (dbHelper.isSpammer(personId) ? 1 : 2);
                    new AlertDialog.Builder(context)
                            .setTitle("Assign a role")
//                            .setMessage("Messages from members with ELITE status will be displayed separately and with SPAMMER status will be given lesser priority")
                            .setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "onClick() returned: " + which);
                                    if (which == 0) {
                                        if (dbHelper.updatePerson(personId, 1, 0)) {
                                            arrayList.set(position, dbHelper.getMember(memberId));
                                            memberAdapter.notifyItemChanged(position);
                                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(context, "Could not update. Please try again", Toast.LENGTH_SHORT).show();
                                    } else if (which == 1) {
                                        if (dbHelper.updatePerson(personId, 0, 1)) {
                                            arrayList.set(position, dbHelper.getMember(memberId));
                                            memberAdapter.notifyItemChanged(position);
                                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(context, "Could not update. Please try again", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (dbHelper.updatePerson(personId, 0, 0)) {
                                            arrayList.set(position, dbHelper.getMember(memberId));
                                            memberAdapter.notifyItemChanged(position);
                                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(context, "Could not update. Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                            }).show();

                }

                @Override
                public void onLongClick(View view, int position) {
                    Member member = dbHelper.getMember(arrayList.get(position).getId());
                    Log.d(TAG, "onLongClick() returned: " + member.getPersonId());
                }
            }));
            if(group.isStore_messages())store_messages.setChecked(true);
            else store_messages.setChecked(false);
            seekBar.setProgress(group.getPriority());
            priority.setText(String.valueOf(group.getPriority()));
            store_messages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (dbHelper.updateGroup(group_id, group.getIcon(), 1, group.getPriority())) {
                            Toast.makeText(context, "Messages will be summarised", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error updating preferences. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        if (dbHelper.updateGroup(group_id, group.getIcon(), 0, group.getPriority())) {
                            Toast.makeText(context, "Messages will not be summarised", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error updating preferences. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    priority.setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (dbHelper.updateGroup(group_id, group.getIcon(), (store_messages.isChecked()) ? 1 : 0, seekBar.getProgress())) {
                        Toast.makeText(context, "Priority set to " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error updating preferences. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}