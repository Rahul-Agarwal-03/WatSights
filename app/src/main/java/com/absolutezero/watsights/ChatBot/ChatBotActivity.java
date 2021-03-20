package com.absolutezero.watsights.ChatBot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;

import com.absolutezero.watsights.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatBotActivity extends AppCompatActivity {
    private static final String TAG = "ChatBotActivity";
    Context context;
    RecyclerView recyclerView;
    ArrayList<Sender> arrayList;
    ChatInterface chatInterface;
    TextInputLayout message1;
    TextInputEditText message;
    CustomAdapter customAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        customAdapter = new CustomAdapter(arrayList, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(customAdapter);
        chatInterface = RetrofitClientInstance.getRetrofitInstance().create(ChatInterface.class);
        message = findViewById(R.id.message);
        message1 = findViewById(R.id.message1);
        getResponse("Hello");
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString().trim();
                if (msg.length() != 0) {
                    arrayList.add(arrayList.size(), new Sender(false, msg));
                    customAdapter.notifyItemInserted(arrayList.size());
                    getResponse(msg);
                    message.setText("");
                    recyclerView.scrollToPosition(arrayList.size() - 1);
                }
            }
        });
    }

    void getResponse(String message) {
        Call<Response> call = chatInterface.getReply(new Request(message));
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d(TAG, "onResponse() returned: " + response.body().getResponse());
                String msg = response.body().getResponse().replace("Doc Bot: ", "");
                arrayList.add(arrayList.size(), new Sender(true, msg));
                customAdapter.notifyItemInserted(arrayList.size());
                recyclerView.scrollToPosition(arrayList.size() - 1);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                arrayList.add(arrayList.size(), new Sender(true, "Sorry! Please try again. Please make sure you are connected to the internet"));

            }

        });
    }
}