package com.abhinandan.familylocater.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abhinandan.familylocater.Models.UserDetails;
import com.abhinandan.familylocater.Models.connections;
import com.abhinandan.familylocater.R;
import com.abhinandan.familylocater.Requests;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UsersRequestAdapter extends RecyclerView.Adapter<UsersRequestAdapter.MyViewHolder> {
    Context context;
    List<UserDetails> list;

    public UsersRequestAdapter(Context context, List<UserDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.itemrequests,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UsersRequestAdapter.MyViewHolder holder, int position) {
        UserDetails u=list.get(position);
        holder.name.setText(u.getContactNo());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Users").child(u.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Connections")){
                            connections l =snapshot.getValue(connections.class);
                            List<String> list1=l.getConnections();
                            if(!list1.contains(FirebaseAuth.getInstance().getUid()))
                            list1.add(FirebaseAuth.getInstance().getUid());
                            FirebaseDatabase.getInstance().getReference("Users").child(u.getId()).child("Connections").setValue(list1);
                        }else{
                            List<String> s=new ArrayList<>();
                            s.add(FirebaseAuth.getInstance().getUid());
                            FirebaseDatabase.getInstance().getReference("Users").child(u.getId()).child("Connections").setValue(s);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Connections")){
                            connections l =snapshot.getValue(connections.class);
                            List<String> list1=l.getConnections();
                            if(!list1.contains(u.getId()))
                            list1.add(u.getId());
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Connections").setValue(list1);
                        }else{
                            List<String> s=new ArrayList<>();
                            s.add(u.getId());
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Connections").setValue(s);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Requests").child("0").removeValue();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Button accept;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
           name= itemView.findViewById(R.id.layoutrequestname);
           accept= itemView.findViewById(R.id.layoutacceptrequest);
        }
    }
}
