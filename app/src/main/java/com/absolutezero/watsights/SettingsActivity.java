package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.absolutezero.watsights.Adapters.PersonAdapter;
import com.absolutezero.watsights.Models.Person;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView1, recyclerView2;
    PersonAdapter personAdapter1, personAdapter2;
    ArrayList<Person> elite, spammers;
    DbHelper dbHelper;
    Context context = this;
    Toolbar toolbar;
    CardView c1, c2, c3;
    RadioButton r1, r2, r3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        dbHelper = new DbHelper(this);
        elite = dbHelper.getImportantPeople();
        spammers = dbHelper.getSpammers();
        personAdapter1 = new PersonAdapter(context, elite);
        personAdapter2 = new PersonAdapter(context, spammers);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
        recyclerView1.setAdapter(personAdapter1);
        recyclerView2.setAdapter(personAdapter2);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}