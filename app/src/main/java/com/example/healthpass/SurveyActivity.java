package com.example.healthpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SurveyActivity extends AppCompatActivity {

    final private String TAG ="SURVEY";
    FirebaseAuth auth;



    RadioGroup radioGroup;
    RadioGroup radioGroup2;
    RadioGroup radioGroup3;
    RadioGroup radioGroup4;
    RadioGroup radioGroup5;
    RadioGroup radioGroup6;

    RadioButton radioButton;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioButton radioButton5;
    RadioButton radioButton6;

    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        radioGroup = findViewById(R.id.radio_group);
        radioGroup2 = findViewById(R.id.radio_group2);
        radioGroup3 = findViewById(R.id.radio_group3);
        radioGroup4 = findViewById(R.id.radio_group4);
        radioGroup5 = findViewById(R.id.radio_group5);
        radioGroup6 = findViewById(R.id.radio_group6);

        textView = findViewById(R.id.text_view_selected);

        auth = FirebaseAuth.getInstance();

        Button button_confirm = findViewById((R.id.button_confirm));


        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                int radioId2 = radioGroup2.getCheckedRadioButtonId();
                int radioId3 = radioGroup3.getCheckedRadioButtonId();
                int radioId4 = radioGroup4.getCheckedRadioButtonId();
                int radioId5 = radioGroup5.getCheckedRadioButtonId();
                int radioId6 = radioGroup6.getCheckedRadioButtonId();


                radioButton = findViewById(radioId);
                radioButton2 = findViewById(radioId2);
                radioButton3 = findViewById(radioId3);
                radioButton4 = findViewById(radioId4);
                radioButton5 = findViewById(radioId5);
                radioButton6 = findViewById(radioId6);

                String str_answer1 = radioButton.getText().toString();
                String str_answer2 = radioButton2.getText().toString();
                String str_answer3 = radioButton3.getText().toString();
                String str_answer4 = radioButton4.getText().toString();
                String str_answer5 = radioButton5.getText().toString();
                String str_answer6 = radioButton6.getText().toString();


                setAnswers(str_answer1,str_answer2,str_answer3,str_answer4,str_answer5,str_answer6);

                textView.setText("Your choice: " + radioButton.getText());
            }
        });

    }

    public void setAnswers(String answer1,String answer2,String answer3,String answer4,String answer5,String answer6){

        FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = firebaseUser.getUid();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userID);
        map.put("answer1", answer1);
        map.put("answer2", answer2);
        map.put("answer3", answer3);
        map.put("answer4", answer4);
        map.put("answer5", answer5);
        map.put("answer6", answer6);

        Toast.makeText(this,TAG,Toast.LENGTH_SHORT).show();
        db.collection("answers")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }




}