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
import com.absolutezero.watsights.Models.Pinned;
import com.absolutezero.watsights.R;

import java.util.ArrayList;

public class PinnedMessageAdapter extends RecyclerView.Adapter<PinnedMessageAdapter.ImportantMessageViewHolder> {

    Context context;
    ArrayList<Pinned> arrayList;
    DbHelper dbHelper;

    public PinnedMessageAdapter(Context context, ArrayList<Pinned> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        dbHelper = new DbHelper(context);
    }


    @NonNull
    @Override
    public ImportantMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_message, parent, false);
        return new ImportantMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImportantMessageViewHolder holder, int position) {
        Message message = dbHelper.getMessage(arrayList.get(position).getMessageId());
        if (message.getGroup_id() != 0) {
            holder.from.setText(dbHelper.getPerson(message.getPerson_id()).getName() +" @ "+ dbHelper.getGroup(message.getGroup_id()).getName());
            holder.from.setTextColor(context.getColor(R.color.ic_launcher_background));
        }
        else{
            holder.from.setText(dbHelper.getPerson(message.getPerson_id()).getName());
            holder.from.setTextColor(context.getColor(R.color.colorAccent));
        }
        holder.message.setText(message.getMessage());
        holder.date.setText(message.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ImportantMessageViewHolder extends RecyclerView.ViewHolder {
        TextView from, message, date;
        public ImportantMessageViewHolder(@NonNull View itemView){
            super(itemView);
            from = itemView.findViewById(R.id.from);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }
    }
}
