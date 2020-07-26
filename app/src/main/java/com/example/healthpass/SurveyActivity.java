package com.example.healthpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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

    EditText etUserTemperature;





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

        etUserTemperature = findViewById(R.id.etUserTemperature);

//      Get current FireAuth Instance
        auth = FirebaseAuth.getInstance();

        Button button_confirm = findViewById((R.id.button_confirm));

//      Submit Button On Click Listener
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//              Retrieve Current Selected Answer Id
                int radioId = radioGroup.getCheckedRadioButtonId();
                int radioId2 = radioGroup2.getCheckedRadioButtonId();
                int radioId3 = radioGroup3.getCheckedRadioButtonId();
                int radioId4 = radioGroup4.getCheckedRadioButtonId();
                int radioId5 = radioGroup5.getCheckedRadioButtonId();
                int radioId6 = radioGroup6.getCheckedRadioButtonId();


//              Set button to the Current Answer ID
                radioButton = findViewById(radioId);
                radioButton2 = findViewById(radioId2);
                radioButton3 = findViewById(radioId3);
                radioButton4 = findViewById(radioId4);
                radioButton5 = findViewById(radioId5);
                radioButton6 = findViewById(radioId6);

//              Get current Value of Selected Answer
                String str_answer1 = radioButton.getText().toString();
                String str_answer2 = radioButton2.getText().toString();
                String str_answer3 = radioButton3.getText().toString();
                String str_answer4 = radioButton4.getText().toString();
                String str_answer5 = radioButton5.getText().toString();
                String str_answer6 = radioButton6.getText().toString();
                float temperature = Float.parseFloat(etUserTemperature.getText().toString());
//              Send answers to DataBase
                setAnswers(str_answer1,str_answer2,str_answer3,str_answer4,str_answer5,str_answer6, temperature);


            }
        });

    }

    public void setAnswers(final String answer1, final String answer2, final String answer3, final String answer4, final String answer5, final String answer6, final float temperature){

        final Boolean pass;

        //getting current date
        final Date currentTime = Calendar.getInstance().getTime();

        //checking for pass/fail criteria
        if (answer1.equals("no") || answer2.equals("no") || answer3.equals("no") || answer4.equals("no") || answer5.equals("no") || answer6.equals("no") ||temperature < 96.8 || temperature > 100.4 ){
            pass = FALSE;
        } else{
            pass =TRUE;
        }

//      Current User Instance
        FirebaseUser firebaseUser = auth.getCurrentUser();
//      Current DB Instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//      Get Current User ID
        final String userID = firebaseUser.getUid();


        //Getting fullname from user database
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //Adding fullname and date to answers string
                    String answers = document.getData().get("fullname").toString();
                    answers += "\n" + currentTime.toString();
                    String issues = "Issues: "+"\n";

                    if(answer1.equals("no"))
                    {
                        issues += "User or household members have Covid-19 symptoms" + "\n";
                    }
                    if(answer2.equals("no"))
                    {
                        issues += "Not tested for Covid-19" + "\n";
                    }
                    if(answer3.equals("no"))
                    {
                        issues += "Visited hospital/healthcare facility within the past 30 days"+ "\n";
                    }
                    if(answer4.equals("no"))
                    {
                        issues += "Travelled outside the USA in the past 21 days"+ "\n";
                    }
                    if(answer5.equals("no"))
                    {
                        issues += "Have been in contact with someone who has Covid-19" + "\n";
                    }
                    if(answer6.equals("no"))
                    {
                        issues += "User or household member is a emergency responder/healthcare provider"+ "\n";
                    }
                    if(temperature < 96.8 || temperature > 100.4)
                    {
                        if(temperature < 96.8)
                        {
                            issues += "Low body temperature " + Float.toString(temperature) + "\n";
                        }
                        else
                        {
                            issues += "High body  temperature " + Float.toString(temperature) + "\n";
                        }

                    }
                    if(pass == false)
                    {
                        answers+= issues + "\n" + "Fail";
                    }
                    else
                    {
                        answers += "Pass";
                    }

                    FirebaseFirestore userDB = FirebaseFirestore.getInstance();
                    userDB.collection("users").document(userID).update("answers", answers);

                    userDB.collection("users").document(userID).update("createdAt", currentTime);

                    Intent i = new Intent(SurveyActivity.this, QRActivity.class);
                    startActivity(i);
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });





//      HashMap with Answer information
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("answer1", answer1);
//        map.put("answer2", answer2);
//        map.put("answer3", answer3);
//        map.put("answer4", answer4);
//        map.put("answer5", answer5);
//        map.put("answer6", answer6);
//        map.put("pass", pass);
//        map.put("date", currentTime);
//      Add HashMap to answers DB
//        db.collection("answers")
//                .add(map)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                        //                      Add answer id to users answers array
//                        FirebaseFirestore userDB = FirebaseFirestore.getInstance();
//                        userDB.collection("users").document(userID).update("answers", FieldValue.arrayUnion(documentReference.getId()));
////                      On Success Return to Main Activity
//                        Intent intent = new Intent(SurveyActivity.this, QRActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });


    }
}