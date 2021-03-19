package com.absolutezero.watsights.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.absolutezero.watsights.DbHelper;
import com.absolutezero.watsights.Models.Message;
import com.absolutezero.watsights.Models.Person;
import com.absolutezero.watsights.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    Context context;
    ArrayList<Message> arrayList;
    DbHelper dbHelper;

    public MessageAdapter(Context context, ArrayList<Message> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.from.setText(dbHelper.getPerson(arrayList.get(position).getPerson_id()).getName());
        holder.message.setText(arrayList.get(position).getMessage());
        holder.date.setText(arrayList.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView from, message, date;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }
    }
}
