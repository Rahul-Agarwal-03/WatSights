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
import com.absolutezero.watsights.Models.Member;
import com.absolutezero.watsights.Models.Person;
import com.absolutezero.watsights.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    Context context;
    ArrayList<Member> arrayList;
    DbHelper dbHelper;

    public MemberAdapter(Context context, ArrayList<Member> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_group_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = arrayList.get(position);
        Person person = dbHelper.getPerson(member.getPersonId());
        holder.name.setText(person.getName());
        if (person.isElite()) {
            holder.spammer.setVisibility(View.GONE);
            holder.important.setVisibility(View.VISIBLE);
        }
        if(person.isSpammer()){
            holder.spammer.setVisibility(View.VISIBLE);
            holder.important.setVisibility(View.GONE);
        } else if (!person.isSpammer() && !person.isElite()) {
            holder.spammer.setVisibility(View.GONE);
            holder.important.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView icon;
        ImageView important, spammer;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
            important = itemView.findViewById(R.id.important);
            spammer = itemView.findViewById(R.id.spammer);
        }
    }
}
