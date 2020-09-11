package com.mojaafar.tutorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class CardActivity extends AppCompatActivity {

    private FirebaseFunctions mFunctions;
    EditText et_card_number,et_expire,et_cvv,et_name;
    String card_number,date,cvv,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        mFunctions = FirebaseFunctions.getInstance();



    }

    public void pay(View view) {
        card_number=et_card_number.getText().toString();
        date=et_expire.getText().toString();
        cvv=et_cvv.getText().toString();
        name=et_name.getText().toString();
        addMessage(card_number,date,cvv,name);
    }
    private Task<String> addMessage(String card_number, String date, String cvv, String name) {

        Map<String, Object> data = new HashMap<>();
        data.put("Number", card_number);
        data.put("date", date);
        data.put("cvv", cvv);
        data.put("name", name);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }
}