package com.abhinandan.familylocater;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.abhinandan.familylocater.Adapters.UsersAdapter;
import com.abhinandan.familylocater.Models.UserDetails;
import com.abhinandan.familylocater.Models.UserLocation;
import com.abhinandan.familylocater.Models.connections;
import com.abhinandan.familylocater.Models.request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements LocationListener {
    DatabaseReference db;
    DatabaseReference dbl;
    EditText id;
    UsersAdapter adapter;
    List<UserDetails> list;
    RecyclerView recyclerView;
    private LocationManager locationManager;
    private final int MIN_TIME=5000;
    private final float MIN_DISTANCE=1;
    EditText searchname;
    String Usernsme;
    ImageView search;
    String ConnectionId;
    SwipeRefreshLayout swipeRefreshLayout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startActivity(new Intent(this,MapsActivity.class));
        setContentView(R.layout.activity_home);
        list=new ArrayList<>();
        locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
        search=findViewById(R.id.button2);
        setSupportActionBar(findViewById(R.id.toolbar));
        db=FirebaseDatabase.getInstance().getReference("Users");
        dbl=FirebaseDatabase.getInstance().getReference("Locations");
        id=findViewById(R.id.editText);
        recyclerView=findViewById(R.id.connectionrecyclerview);
        adapter=new UsersAdapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        updateconnections();
        getlocationupdate();
//        setchangedlocation();

    swipeRefreshLayout=findViewById(R.id.swiperefresh);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(Home.this, "refresh", Toast.LENGTH_SHORT).show();
            list=new ArrayList<>();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    });
    }

    private void updateconnections() {

        db.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(snapshot.hasChild("Connections")){
                    connections c=snapshot.getValue(connections.class);
                    List<String> l=c.getConnections();
                    if(l==null) Toast.makeText(Home.this, "No Connections", Toast.LENGTH_SHORT).show();
                    for(String s:l){
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(s)){
                                    db.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            list.add(snapshot.getValue(UserDetails.class));
                                            adapter.notifyDataSetChanged();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Requests:
                startActivity(new Intent(this,Requests.class));
                return true;
            case R.id.menuLogut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,MainActivity.class));
//                Toast.makeText(this, "loggedout", Toast.LENGTH_SHORT).show();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void getlocationupdate() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
        ) {
            if (locationManager != null) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                else
                    Toast.makeText(this, "No PRovider For Location is enabled", Toast.LENGTH_SHORT).show();
            }
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void searchbutton(View view) {

       if(Usernsme==null) {
            if (id.getText().toString().equals(""))
                Toast.makeText(this, "Enter Id", Toast.LENGTH_SHORT).show();
            else {
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(id.getText().toString())) {
                            search.setImageResource(R.drawable.add);
                            ConnectionId=id.getText().toString();
                            db.child(id.getText().toString()).child("contactNo").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    String name=snapshot.getValue(String.class);
                                    id.setText(name);
                                    Usernsme=name;
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });


                        } else
                            Toast.makeText(Home.this, "Enter a Valid Id", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        }else{

           db.child(ConnectionId).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                   if (snapshot.hasChild("Requests")) {
                       request c = snapshot.getValue(request.class);
                       List<String> c1 = c.getRequests();
                       if (c1.contains(FirebaseAuth.getInstance().getUid()))
                           Toast.makeText(Home.this, "Request Already sent", Toast.LENGTH_SHORT).show();
                       else
                           c1.add(FirebaseAuth.getInstance().getUid());
                       db.child(ConnectionId).child("Requests")
                               .setValue(c1);
                   } else {
                       List<String> connections = new ArrayList<>();
                       connections.add(FirebaseAuth.getInstance().getUid());
                       db.child(ConnectionId).child("Requests").setValue(connections);
                   }
                   search.setImageResource(R.drawable.send);
                   Usernsme=null;
                   id.setText("");
                   Toast.makeText(Home.this, "Request Sent", Toast.LENGTH_SHORT).show();

               }

               @Override
               public void onCancelled(@NonNull @NotNull DatabaseError error) {

               }
           });

       }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getlocationupdate();
                }else Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location!=null){
            savelocation(location);
        }else{
            Toast.makeText(this, "No Location Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void savelocation(Location location) {
        dbl.child(FirebaseAuth.getInstance().getUid()).child("location").setValue(location);
    }


}