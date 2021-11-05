package com.abhinandan.familylocater.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhinandan.familylocater.MainActivity;
import com.abhinandan.familylocater.MapsActivity;
import com.abhinandan.familylocater.Models.UserDetails;
import com.abhinandan.familylocater.R;
import com.abhinandan.familylocater.Register;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    Context context;
    List<UserDetails> list;

    public UsersAdapter(Context context, List<UserDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.itemconnection,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UsersAdapter.MyViewHolder holder, int position) {
        UserDetails u=list.get(position);
        holder.name.setText(u.getContactNo());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,MapsActivity.class);
                i.putExtra("id",u.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
           name= itemView.findViewById(R.id.LayoutName);
        }
    }
}
