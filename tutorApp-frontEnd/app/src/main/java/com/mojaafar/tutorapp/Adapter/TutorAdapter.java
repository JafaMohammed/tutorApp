package com.mojaafar.tutorapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.mojaafar.tutorapp.PaymentActivity;
import com.mojaafar.tutorapp.R;

import com.mojaafar.tutorapp.Models.Tutor;
import com.mojaafar.tutorapp.Tutorb;
import com.mojaafar.tutorapp.ui.dashboard.DashboardFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Tutor> mPosts;

    private FirebaseUser firebaseUser;
    //defining global variable

    public TutorAdapter(Context context, List<Tutor> posts){
        mContext = context;
        mPosts = posts;
        //initializing
    }

    @NonNull
    @Override
    public TutorAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_tutor, parent, false);
        return new TutorAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TutorAdapter.ImageViewHolder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Tutor post = mPosts.get(position);
        //initializing

        Glide.with(mContext).load(post.getImageurl())
                .into(holder.image_profile);
        holder.username.setText(post.getFullname());
        holder.subject.setText(post.getSubject());
        holder.comment.setText(post.getBio());
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Tutorb.class);
                intent.putExtra(PaymentActivity.KEY_OFFER_ID, post.getPrice());
                intent.putExtra(PaymentActivity.KEY_USER_ID, post.getUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile;
        public TextView username, comment,subject;
        RelativeLayout main;
        //defining

        public ImageViewHolder(View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.profile_picture);
            username = itemView.findViewById(R.id.user_name);
            comment = itemView.findViewById(R.id.bio);
            main=itemView.findViewById(R.id.main);
            subject=itemView.findViewById(R.id.subject);
        }
    }

    String notiId=null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


}