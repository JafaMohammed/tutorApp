package com.mojaafar.tutorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class signup extends AppCompatActivity {
    EditText usrusr,pswrdd,names,mobphone;
    String username,password,fname,phone;
    String role="Tutor";
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        pswrdd=findViewById(R.id.pswrdd);
        names=findViewById(R.id.names);
        mobphone=findViewById(R.id.mobphone);
        usrusr=findViewById(R.id.mail);
        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(signup.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void register(View view) {
        pd = new ProgressDialog(signup.this);
        pd.setMessage("Please wait...");

        username=usrusr.getText().toString();
        password=pswrdd.getText().toString();
        fname=names.getText().toString();
        phone=mobphone.getText().toString();
        if (validate()){
            pd.show();
            mAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())  {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String userID = Objects.requireNonNull(firebaseUser).getUid();

                                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Email", username);
                                map.put("fullname", fname);
                                map.put("phone", phone);
                                map.put("role",role);
                                map.put("uid",userID);
                                reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            pd.dismiss();
                                            if (role.equals("Student")){
                                                Intent intent = new Intent(signup.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Intent intent = new Intent(signup.this, MainActivity2.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(signup.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }


    }

    public void login(View view) {
        Intent intent = (new Intent(this, login.class));
        this.startActivity(intent);
    }
    public boolean validate(){
        boolean valid=true;
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            usrusr.setError("Enter valid email");
            valid=false;
        }else {usrusr.setError(null);}
        if(fname.length()<3){
            names.setError("Enter a valid name");
            valid=false;
        }else {names.setError(null);}
        if(phone.length()<12){
            mobphone.setError("Enter a valid phone number");
            valid=false;
        }else {mobphone.setError(null);}

        if(password.length()<6){
            pswrdd.setError("Password must be more than 6 char long");
            valid=false;
        }else {pswrdd.setError(null);}
        return valid;
    }
}