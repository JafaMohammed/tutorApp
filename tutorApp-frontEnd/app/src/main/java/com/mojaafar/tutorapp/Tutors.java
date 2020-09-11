package com.mojaafar.tutorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mojaafar.tutorapp.Adapter.TutorAdapter;
import com.mojaafar.tutorapp.Models.Tutor;

import java.util.ArrayList;
import java.util.List;

public class Tutors extends AppCompatActivity {
    RecyclerView recycler;
    TutorAdapter postAdapter;
    List<Tutor> tutorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutors);

        recycler =findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(Tutors.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(mLayoutManager);
        tutorList = new ArrayList<>();
        postAdapter = new TutorAdapter(Tutors.this, tutorList);
        recycler.setAdapter(postAdapter);
        readPosts();
    }
    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        //Query query=reference.child("Role").equalTo("Tutor");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tutorList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Tutor tutor = snapshot.getValue(Tutor.class);
                    tutorList.add(tutor);
                }

                postAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}