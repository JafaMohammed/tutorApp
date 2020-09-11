package com.mojaafar.tutorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mojaafar.tutorapp.Models.User;

import java.util.HashMap;
import java.util.Objects;

import static java.security.AccessController.getContext;

public class EditProf extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String uid;
    EditText name,email,phone;
    ImageView user_image;
    ProgressDialog pd;
    String nam,phon,emai;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof);
        mAuth = FirebaseAuth.getInstance();
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);

        user_image=findViewById(R.id.user_image);
        if(mAuth.getCurrentUser()!=null){
            uid= mAuth.getCurrentUser().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (getContext() == null){
                        return;
                    }
                    User user = dataSnapshot.getValue(User.class);
                    name.setText(user.getFullname());
                    email.setText(user.getEmail());
                    phone.setText(user.getPhone());
                    Glide.with(EditProf.this).load(user.getImageurl()).into(user_image);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void save(View view) {
        pd = new ProgressDialog(EditProf.this);
        pd.setMessage("Please wait...");

        nam=name.getText().toString();
        phon=phone.getText().toString();
        emai=email.getText().toString();
        if (validate()){
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            HashMap<String, Object> map = new HashMap<>();
            map.put("Email", emai);
            map.put("fullname", nam);
            map.put("phone", phon);
            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        pd.dismiss();
                        Toast.makeText(EditProf.this, "Update Succesful.",
                                Toast.LENGTH_SHORT).show();
                       finish();

                    }else {
                        Toast.makeText(EditProf.this, "Update Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    private boolean validate() {
        boolean valid=true;
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emai).matches()){
            email.setError("Enter valid email");
            valid=false;
        }else {email.setError(null);}

        if(nam.length()<3){
            name.setError("Enter a valid name");
            valid=false;
        }else {name.setError(null);}
        if(phon.length()<3){
            phone.setError("Enter a valid phonenumber");
            valid=false;
        }else {phone.setError(null);}
        return valid;

    }
}