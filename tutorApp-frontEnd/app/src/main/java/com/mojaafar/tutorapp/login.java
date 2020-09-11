package com.mojaafar.tutorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mojaafar.tutorapp.http.ApiEndpoint;
import com.mojaafar.tutorapp.Models.User;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mojaafar.tutorapp.utils.Utils.BASE_URL;
import static java.security.AccessController.getContext;

public class login extends AppCompatActivity {

    EditText usrusr,pswrdd;
    String username,password;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        pswrdd=findViewById(R.id.pswrdd);
        usrusr=findViewById(R.id.usrusr);
        if(mAuth.getCurrentUser()!=null){
           uid= mAuth.getCurrentUser().getUid();
           checkrole();
        }
    }
    public void loginUser(){
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndpoint endpoint = retrofit.create(ApiEndpoint.class);
       // Call<User> call = endpoint.loginUser();
    }
    public void signup(View view){
        Intent intent = (new Intent(this, signup.class));
        this.startActivity(intent);
    }
    public void resetpass(View view){
        Intent intent = (new Intent(this, ResetPass.class));
        this.startActivity(intent);
    }

    public void signin(View view) {
        pd = new ProgressDialog(login.this);
        pd.setMessage("Please wait...");

        username=usrusr.getText().toString();
        password=pswrdd.getText().toString();
        if (validate()){
            pd.show();
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                uid= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                               checkrole();
                               pd.dismiss();
                            } else {
                                // If sign in fails, display a message to the user
                                pd.dismiss();
                                Toast.makeText(login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }

    }
    public void checkrole(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                User user = dataSnapshot.getValue(User.class);
                if (Objects.requireNonNull(user).getRole().equals("Student")){
                    Intent intent = new Intent(login.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(login.this, MainActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public boolean validate(){
        boolean valid=true;
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            usrusr.setError("Enter valid email");
            valid=false;
        }else {usrusr.setError(null);}

        if(password.length()<6){
            pswrdd.setError("Password must be more than 6 char long");
            valid=false;
        }else {pswrdd.setError(null);}
        return valid;
    }

}