package com.absolutezero.watsights.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.absolutezero.watsights.DbHelper;
import com.absolutezero.watsights.Models.Group;
import com.absolutezero.watsights.Models.Message;
import com.absolutezero.watsights.R;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    Context context;
    ArrayList<Group> arrayList;
    DbHelper dbHelper;
    public GroupAdapter(Context context, ArrayList<Group> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_chat, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = arrayList.get(position);
        int unread = dbHelper.getGroupMessages(group.getId()).size();
        holder.icon.setCircleBackgroundColor(group.getIcon());
        Date date = new Date(group.getLast_viewed());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
        holder.name.setText(group.getName());
        holder.date.setText(simpleDateFormat.format(date));
        holder.number.setText(String.valueOf(unread));
        //TODO
        ArrayList<Message> arrayList = dbHelper.getGroupMessages(group.getId());
        holder.summary.setText(arrayList.get(arrayList.size() - 1).getMessage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        CircleImageView icon;
        TextView name,summary,date;
        Chip number;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            summary = itemView.findViewById(R.id.summary);
            date = itemView.findViewById(R.id.date);
            number = itemView.findViewById(R.id.number);
        }
    }

}
