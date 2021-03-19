package com.absolutezero.watsights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.webkit.WebView;

public class ChatBotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        WebView webview;
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(getString(R.string.app_name));
        webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        String html = "<iframe width=\""+"350"+"\" height=\"430\" allow=\"microphone;\" src=\"https://console.dialogflow.com/api-client/demo/embedded/6f7df4cf-dc53-46f3-a705-68052771016b\"></iframe>";
        webview.loadData(html, "text/html", null);
    }
}