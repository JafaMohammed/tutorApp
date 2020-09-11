package com.mojaafar.tutorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mojaafar.tutorapp.Models.Tutor;
import com.mojaafar.tutorapp.Models.User;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

public class Tutorb extends AppCompatActivity {
    public static final String KEY_OFFER_ID = "key_offer_id";
    public static final String KEY_USER_ID = "key_user_id";
    String taskId,user_id,offer,payer,phone_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        taskId = Objects.requireNonNull(getIntent().getExtras()).getString(KEY_OFFER_ID);
        user_id=Objects.requireNonNull(getIntent().getExtras()).getString(KEY_USER_ID);
        if (user_id==null){
            throw new IllegalArgumentException("Must pass extra " + KEY_USER_ID);
        }
        if (taskId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_OFFER_ID);
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                Tutor user = dataSnapshot.getValue(Tutor.class);
                TextView txt_phone=findViewById(R.id.abt);
                TextView userimg=findViewById(R.id.user_name);
                userimg.setText(Objects.requireNonNull(user).getFullname());
                CircleImageView user_image=findViewById(R.id.user_image);
                Glide.with(Tutorb.this).load(user.getImageurl())
                        .into(user_image);
                txt_phone.setText(user.getBio());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void book(View view) {
        Intent intent = new Intent(Tutorb.this, Tutorb.class);
        intent.putExtra(PaymentActivity.KEY_OFFER_ID, user_id);
        intent.putExtra(PaymentActivity.KEY_USER_ID, taskId);
        startActivity(intent);
    }
}