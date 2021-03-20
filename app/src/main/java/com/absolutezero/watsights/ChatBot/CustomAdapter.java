package com.absolutezero.watsights.ChatBot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.absolutezero.watsights.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    ArrayList<Sender> arrayList;
    Context context;

    public CustomAdapter(ArrayList<Sender> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_bot_message, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (arrayList.get(position).isBot()) {
            holder.user.setVisibility(View.GONE);
            holder.bot.setVisibility(View.VISIBLE);
            holder.from_bot.setText(arrayList.get(position).getMessage());
        } else {
            holder.bot.setVisibility(View.GONE);
            holder.user.setVisibility(View.VISIBLE);
            holder.from_user.setText(arrayList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView from_user,from_bot;
        CardView user, bot;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            from_user = itemView.findViewById(R.id.from_user);
            from_bot = itemView.findViewById(R.id.from_bot);
            user = itemView.findViewById(R.id.user);
            bot = itemView.findViewById(R.id.bot);
        }
    }
}
