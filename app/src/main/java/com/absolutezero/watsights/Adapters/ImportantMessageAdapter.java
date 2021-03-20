package com.absolutezero.watsights.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.absolutezero.watsights.DbHelper;
import com.absolutezero.watsights.Models.Important;
import com.absolutezero.watsights.Models.Message;
import com.absolutezero.watsights.R;

import java.util.ArrayList;

public class ImportantMessageAdapter extends RecyclerView.Adapter<ImportantMessageAdapter.ImportantMessageViewHolder> {

    Context context;
    ArrayList<Message> arrayList;
    DbHelper dbHelper;

    public ImportantMessageAdapter(Context context, ArrayList<Message> arrayList) {
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
        if (arrayList.get(position).getGroup_id() != 0) {
            holder.from.setText(dbHelper.getGroup(arrayList.get(position).getGroup_id()).getName());
            holder.from.setTextColor(context.getColor(R.color.ic_launcher_background));
        }
        else{
            holder.from.setText("Personal");
            holder.from.setTextColor(context.getColor(R.color.colorAccent));
        }
        holder.message.setText(arrayList.get(position).getMessage());
        holder.date.setText(arrayList.get(position).getTimestamp());
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
