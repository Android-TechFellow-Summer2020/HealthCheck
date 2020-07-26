package com.example.healthpass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final private String TAG = "MainActivity";
    Button buttonStartResourceActivity;
    Button buttonStartSurvey;
    Button btnLogout;
    Button btnQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Button Declarations
        buttonStartResourceActivity = findViewById(R.id.button_resources);
        buttonStartSurvey = findViewById(R.id.button_start_survey);
        btnLogout =findViewById(R.id.logoutBtn);
        btnQRCode = findViewById(R.id.btnQRCode);

//      Get Current User
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String userID = firebaseUser.getUid();


//      OnClick Listeners for buttons
        buttonStartSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSurvey();
            }
        });

        buttonStartResourceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startResourceActivity();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Logging out.");
                //logs user out using firebase
                Log.d(TAG, "logging off: "+auth.getCurrentUser());
                signOut(auth);
                goToStartActivity();
            }
        });

        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("users").document(userID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if( document.getData().get("answers").equals(new ArrayList<>()))
                                {
                                    Toast.makeText(MainActivity.this, "Need to fill survey first!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    goToQRCodeActivity();
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });



    }

    private void startSurvey() {
//      Goes to Survey Activity
        Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
        startActivity(intent);
    }
    private void startResourceActivity() {
//      Goes to Resource Activity
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }
    private void goToStartActivity() {
//      Goes to Start Activity
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
    }

    private void goToQRCodeActivity() {
        Intent i = new Intent(MainActivity.this, QRActivity.class);
        startActivity(i);
    }
    private void signOut(FirebaseAuth instance)
    {
//      Ends users session
        instance.signOut();
    }
}
