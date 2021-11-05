package com.abhinandan.familylocater;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinandan.familylocater.Models.UserDetails;
import com.abhinandan.familylocater.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    private TextView welcome;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance().getReference("Users");
        welcome=binding.welcometext;
        binding.progressbar.setVisibility(View.INVISIBLE);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.tvUserName.getText().toString().equals("")){
                    binding.tvUserName.setError("Enter Email");
                }
                else if(binding.tvPassword.getText().toString().equals(""))binding.tvPassword.setError("Enter Password");
                else if(binding.tvCnfpassword.getText().toString().equals(""))binding.tvCnfpassword.setError("Enter Password");
                else if(binding.tvContactno.getText().toString().equals(""))binding.tvContactno.setError("Enter Contact Number");
                else if(!(binding.tvPassword.getText().toString().equals(binding.tvCnfpassword.getText().toString())))binding.tvCnfpassword.setError("Passwords Do Not Match");
                else{
                    binding.progressbar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    firebaseAuth.createUserWithEmailAndPassword(binding.tvUserName.getText().toString(),binding.tvPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                binding.progressbar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                UserDetails u=new UserDetails(binding.tvUserName.getText().toString(),binding.tvPassword.getText().toString(),binding.tvContactno.getText().toString(),firebaseAuth.getUid());
                                db.child(firebaseAuth.getUid()).setValue(u);
                                Toast.makeText(Register.this, "LoginSucessfull", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getApplicationContext(),Home.class);
                                Pair[] p=new Pair[1];
                                p[0]=new Pair<>(welcome,"welcome");
                                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(Register.this,p).toBundle());

                            }else Toast.makeText(Register.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

}