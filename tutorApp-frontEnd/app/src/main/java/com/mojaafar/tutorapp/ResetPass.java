package com.mojaafar.tutorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {
    EditText email_edt;
    String email;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        email_edt=findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
    }

    public boolean validate() {
        boolean valid = true;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_edt.setError("Enter valid email");
            valid = false;
        }else  {
            email_edt.setError(null);

        }
        return valid;
    }
    public void back(View view) {
        finish();

    }
    public void next(View view) {
        email=email_edt.getText().toString();
        if (validate()){
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ResetPass.this, "Email=  sent to"+email, Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ResetPass.this, "Email not sent"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

    }
}