package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.absolutezero.watsights.Adapters.ImportantMessageAdapter;
import com.absolutezero.watsights.ChatBot.ChatBotActivity;
import com.absolutezero.watsights.Models.Important;
import com.absolutezero.watsights.Models.Message;
import com.absolutezero.watsights.Models.Spam;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    CardView c1, c2, c3, c4, c5, c6;
    DbHelper dbHelper;
    Context context = this;
    PieChart pieChart;
    RecyclerView recyclerView;
    ArrayList<Important> arrayList;
    ImportantMessageAdapter importantMessageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO PIECHART - NO OF MESSAGES PER GROUP
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        pieChart = findViewById(R.id.pieChart);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        dbHelper = new DbHelper(context);
        arrayList = dbHelper.getImportantMessages();
        if (arrayList.size() == 0) {
            findViewById(R.id.notFound).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.notFound).setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            importantMessageAdapter = new ImportantMessageAdapter(context, arrayList);
            recyclerView.setAdapter(importantMessageAdapter);
        }
        setPieChart();
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://yashkasera.github.io/watsightsweb.github.io/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void setPieChart() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(this, R.color.orange));
        colors.add(ContextCompat.getColor(this, R.color.red));
        colors.add(ContextCompat.getColor(this, R.color.pie_green_3));
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        int imp = (dbHelper.getEliteMessages() != null) ? dbHelper.getEliteMessages().size() : 0;
        int spa = (dbHelper.getSpamMessages() != null) ? dbHelper.getSpamMessages().size() : 0;
        for (Spam spam : dbHelper.getSpamMessages()) {
            Message message = dbHelper.getMessage(spam.getMessageId());
            Log.d(TAG, "setPieChart() returned: " + message.getMessage());
        }
        int tot = (dbHelper.getMessages() != null) ? dbHelper.getMessages().size() : 0;
        int other = tot - (imp + spa);
        entries.add(new PieEntry(imp, "Important"));
        entries.add(new PieEntry(spa, "Spam"));
        entries.add(new PieEntry(other, "Other"));
        PieDataSet dataset = new PieDataSet(entries, "");
        PieData data = new PieData(dataset);        // initialize Piedata
        pieChart.setData(data);
        dataset.setColors(colors);
        pieChart.notifyDataSetChanged();
        pieChart.setHoleColor(ContextCompat.getColor(this, android.R.color.transparent));
        Legend l = pieChart.getLegend();
//        l.setTextSize(16f);
//        l.setFormSize(12f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setTextColor(Color.BLACK);
        l.setEnabled(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
//        pieOrders.setEntryLabelColor(Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
//                startActivity(new Intent());
            case R.id.c1:
                startActivity(new Intent(this, ViewGroupsActivity.class));
                break;
            case R.id.c2:
                startActivity(new Intent(this, SendMessageActivity.class));
                break;
            case R.id.c3:
                startActivity(new Intent(this, ViewPinnedMessagesActivity.class));
                break;
            case R.id.c4:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.c5:
                startActivity(new Intent(this, ViewElitePeopleActivity.class));
                break;
            case R.id.c6:
                startActivity(new Intent(this, ChatBotActivity.class));
                break;
        }
    }
}