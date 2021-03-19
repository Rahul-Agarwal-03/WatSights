package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.absolutezero.watsights.Models.Important;
import com.absolutezero.watsights.Models.Message;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    Button b0;
    CardView c1,c2,c3,c4;
    DbHelper dbHelper;
    Context context = this;
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO PIECHART - NO OF MESSAGES PER GROUP
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        pieChart = findViewById(R.id.pieChart);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        dbHelper = new DbHelper(context);
        setPieChart();
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatBotActivity.class));
            }
        });
    }

    private void setPieChart() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        colors.add(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
        colors.add(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        int imp = (dbHelper.getImportantMessages() != null) ? dbHelper.getImportantMessages().size() : 0;
        int spa = (dbHelper.getSpamMessages() != null) ? dbHelper.getImportantMessages().size() : 0;
        int tot = (dbHelper.getMessages() != null) ? dbHelper.getImportantMessages().size() : 0;
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
        }
    }
}