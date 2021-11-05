package com.abhinandan.familylocater;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinandan.familylocater.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    TextView register;
    TextView welcome,conti,usernamename,passwordname;
    LinearLayout donthave;
    CardView card;
    Button login;
    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;
    ImageView logo;
    ConstraintLayout username,password;
    Button button;
    EditText username_for_signup,user_password_for_signup;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getUid()!=null){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        register=findViewById(R.id.signuptext);
        donthave=findViewById(R.id.donthave);
        button=findViewById(R.id.buttonsignin);
        conti=findViewById(R.id.signintocontinue);
        card=findViewById(R.id.cardView2);
        usernamename=findViewById(R.id.tv_user_name_title);
        passwordname=findViewById(R.id.tv_password_title);
        password=findViewById(R.id.constraintLayout1);
        welcome=findViewById(R.id.login);
        login=findViewById(R.id.buttonsignin);
        logo=findViewById(R.id.logo);
        username_for_signup=findViewById(R.id.tv_user_name1);
        user_password_for_signup=findViewById(R.id.tv_password);
        binding.progressbar.setVisibility(View.INVISIBLE);
        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
               Intent i=new Intent(getApplicationContext(),Register.class);
               Pair [] p=new Pair[7];
                p[0]=new Pair<>(welcome,"welcome");
                p[1]=new Pair<>(logo,"logo");
                p[2]=new Pair<>(card,"card");
//                p[3]=new Pair<>(button,"button");
                p[3]=new Pair<>(donthave,"donthave");
                p[4]=new Pair<>(conti,"continue");
                p[5]=new Pair<>(usernamename ,"usernamename");
                p[6]=new Pair<>(passwordname,"passwordname");
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,p).toBundle());
            }
        });
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(username_for_signup.getText().toString().equals("")) username_for_signup.setError("Enter Email");
               else if(user_password_for_signup.getText().toString().equals(""))user_password_for_signup.setError("enter Password");
               else{
                   binding.progressbar.setVisibility(View.VISIBLE);
                   getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                           WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                   firebaseAuth.signInWithEmailAndPassword(username_for_signup.getText().toString(),user_password_for_signup.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                       @Override
                       public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               binding.progressbar.setVisibility(View.INVISIBLE);
                               getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                               Intent i=new Intent(getApplicationContext(),Home.class);
                               Pair [] p=new Pair[1];
                               p[0]=new Pair<>(welcome,"welcome");
                               startActivity(i, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,p).toBundle());

                           }else Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }
       });

    }
}