package com.mojaafar.tutorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mojaafar.tutorapp.Models.User;
import com.mojaafar.tutorapp.api.ApiClient;
import com.mojaafar.tutorapp.api.model.AccessToken;
import com.mojaafar.tutorapp.api.model.STKPush;
import com.mojaafar.tutorapp.util.NotificationUtils;
import com.mojaafar.tutorapp.util.SharedPrefsUtil;
import com.mojaafar.tutorapp.util.Utils;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mojaafar.tutorapp.util.AppConstants.BUSINESS_SHORT_CODE;
import static com.mojaafar.tutorapp.util.AppConstants.CALLBACKURL;
import static com.mojaafar.tutorapp.util.AppConstants.PARTYB;
import static com.mojaafar.tutorapp.util.AppConstants.PASSKEY;
import static com.mojaafar.tutorapp.util.AppConstants.PUSH_NOTIFICATION;
import static com.mojaafar.tutorapp.util.AppConstants.REGISTRATION_COMPLETE;
import static com.mojaafar.tutorapp.util.AppConstants.TRANSACTION_TYPE;
import static java.security.AccessController.getContext;

public class PaymentActivity extends AppCompatActivity {
    public static final String KEY_OFFER_ID = "key_offer_id";
    public static final String KEY_USER_ID = "key_user_id";


    private ProgressDialog mProgressDialog;
    private String mFireBaseRegId;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SharedPrefsUtil mSharedPrefsUtil;
    private ApiClient mApiClient;
    String taskId,user_id,offer,payer,phone_no;
    Switch switch1;
    EditText edit;
    Boolean other=false;
    String namba,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mProgressDialog = new ProgressDialog(this);
        mSharedPrefsUtil = new SharedPrefsUtil(this);
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.
        switch1=findViewById(R.id.switch1);
        edit=findViewById(R.id.phone_no);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    // do something when check is selected
                    other=true;
                    edit.setVisibility(View.VISIBLE);

                } else {
                    //do something when unchecked
                    edit.setVisibility(View.GONE);
                    other=false;

                }
            }
        });
       // phone_no=Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber().substring(1);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
                    TextView txt_phone=findViewById(R.id.txt_phone);
                    phone_no=user.getPhone();
                    txt_phone.setText(user.getPhone());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        taskId = Objects.requireNonNull(getIntent().getExtras()).getString(KEY_OFFER_ID);
        user_id=Objects.requireNonNull(getIntent().getExtras()).getString(KEY_USER_ID);
        if (user_id==null){
            throw new IllegalArgumentException("Must pass extra " + KEY_USER_ID);
        }
        if (taskId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_OFFER_ID);
        }
        TextView offe=findViewById(R.id.offer);
        offe.setText("Ksh " + offer +" /-");



        getAccessToken();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    // FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
                    //getFirebaseRegId();

                } else if (intent.getAction().equals(PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    NotificationUtils.createNotification(getApplicationContext(), message);
                    showResultDialog(message);
                }
            }
        };





    }
    private void getFirebaseRegId() {
        mFireBaseRegId = mSharedPrefsUtil.getFirebaseRegistrationID();

        if (!TextUtils.isEmpty(mFireBaseRegId)) {
            mSharedPrefsUtil.saveFirebaseRegistrationID(mFireBaseRegId);
        }
    }

    public void showResultDialog(String result) {
        Timber.d(result);
        if (!mSharedPrefsUtil.getIsFirstTime()) {
            // run your one time code
            mSharedPrefsUtil.saveIsFirstTime(true);

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText("Payment Successful")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            mSharedPrefsUtil.saveIsFirstTime(false);
                        }
                    })
                    .show();
        }
    }
    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    public void pay(View view) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Processing payment");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        if (other){
            if (validate()){
                phone_no=namba;
                performSTKPush(namba);
            }

        }else {
            performSTKPush(phone_no);
        }

        // performSTKPush("+254745381950","test");
       /* if (phone_no.getVisibility() == View.VISIBLE) {
            String no= phone_no.getText().toString();
            // Its visible
            if (!no.isEmpty()) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setTitle("Processing payment");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                performSTKPush("+254745381950","test");
            }
        } else {
            // Either gone or invisible
            if (phone_number != null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setTitle("Processing payment");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                performSTKPush("+254745381950","test");
            }
        }*/
    }
    public void performSTKPush(String phone_number) {
        mProgressDialog.setMessage("Processing payment");
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                "1",
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                taskId, //The account reference
                "test"  //The transaction description
        );

        mApiClient.setGetAccessToken(false);

        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                        //Toast.makeText(PaymentActivity.this,response.errorBody().string(),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }
    public boolean validate() {
        boolean valid = true;
        namba=edit.getText().toString();
        if (namba.isEmpty() || namba.length() < 10 || namba.length() == 11 || namba.length()>13) {
            edit.setError("Enter a valid phone number");
            valid = false;
        } else {
            edit.setError(null);
        }

        return valid;
    }

    public void next(View view) {
        Intent intent = new Intent(PaymentActivity.this, CardActivity.class);

        startActivity(intent);
    }
}