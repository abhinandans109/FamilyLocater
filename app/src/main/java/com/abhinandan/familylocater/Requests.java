package com.abhinandan.familylocater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.abhinandan.familylocater.Adapters.UsersRequestAdapter;
import com.abhinandan.familylocater.Models.UserDetails;
import com.abhinandan.familylocater.Models.request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Requests extends AppCompatActivity {
    RecyclerView recyclerView;
    UsersRequestAdapter usersRequestAdapter1;
    List<UserDetails> list;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.requestrecyclerview);
        usersRequestAdapter1=new UsersRequestAdapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(usersRequestAdapter1);
        db=FirebaseDatabase.getInstance().getReference("Users");

        db.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild("Requests")){
                    request r=snapshot.getValue(request.class);
                    List<String> l=r.getRequests();
                    if(l==null){
                        Toast.makeText(Requests.this, "No requests", Toast.LENGTH_SHORT).show();
                    }
                    for(String s:l){
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(s)){
                                    db.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            list.add(snapshot.getValue(UserDetails.class));
                                            Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_SHORT).show();
                                            usersRequestAdapter1.notifyDataSetChanged();


                                        }


                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}