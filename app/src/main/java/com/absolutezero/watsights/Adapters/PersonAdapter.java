package com.absolutezero.watsights.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.absolutezero.watsights.DbHelper;
import com.absolutezero.watsights.Models.Person;
import com.absolutezero.watsights.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    Context context;
    ArrayList<Person> arrayList;
    DbHelper dbHelper;

    public PersonAdapter(Context context, ArrayList<Person> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_group_member, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = arrayList.get(position);
        holder.name.setText(person.getName());
        if (person.isImportant()) {
            holder.spammer.setVisibility(View.GONE);
            holder.important.setVisibility(View.VISIBLE);
        }
        if(person.isSpammer()){
            holder.spammer.setVisibility(View.VISIBLE);
            holder.important.setVisibility(View.GONE);
        } else if (!person.isSpammer() && !person.isImportant()) {
            holder.spammer.setVisibility(View.GONE);
            holder.important.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView icon;
        ImageView important, spammer;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
            important = itemView.findViewById(R.id.important);
            spammer = itemView.findViewById(R.id.spammer);
        }
    }
}
