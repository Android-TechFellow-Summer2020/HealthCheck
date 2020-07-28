package com.example.healthpass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final private String TAG = "MainActivity";
    Button buttonStartResourceActivity;
    Button buttonStartSurvey;
    Button btnLogout;
    Button btnQRCode;
    Button btnLocationHistory;
    TextView tvTimeDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Button Declarations
        buttonStartResourceActivity = findViewById(R.id.button_resources);
        buttonStartSurvey = findViewById(R.id.button_start_survey);
        btnLogout =findViewById(R.id.logoutBtn);
        btnQRCode = findViewById(R.id.btnQRCode);
        btnLocationHistory = findViewById(R.id.btnLocationHistory);
        tvTimeDifference = findViewById(R.id.tvTimeDifference);


//      Get Current User
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String userID = firebaseUser.getUid();

        displayTimeDifference(db, userID);

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

    btnLocationHistory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToLocationHistoryActivity();
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
        Intent intent = new Intent(MainActivity.this, ResourcesActivity.class);
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

    private void goToLocationHistoryActivity() {
        Intent i = new Intent(MainActivity.this, LocationHistoryActivity.class);
        startActivity(i);
    }


    private void signOut(FirebaseAuth instance)
    {
//      Ends users session
        instance.signOut();
    }

    private void displayTimeDifference(final FirebaseFirestore db, final String userID)
    {
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Timestamp createdAt = (Timestamp) document.getData().get("createdAt");
                    Date createdAtDate = createdAt.toDate();
                    final Date currentTime = Calendar.getInstance().getTime();
                    long difference = currentTime.getTime() - createdAtDate.getTime();
                    double day = difference/86400000;
                    difference = difference % (84600000);
                    double hours = difference/3600000;
                    difference %= 3600000;
                    double minutes = difference/60000;
                    difference %= 60000;
                    double seconds = difference/1000;
                    String time = String.valueOf((int)day) + " day(s), " + String.valueOf((int) hours) + " hour(s), " + String.valueOf((int) minutes) + " minute(s), " + String.valueOf((int) seconds) + " second(s)" + "\n";
                    time += "You must update your survey after 24 hours.";
                    tvTimeDifference.setText(time);
                    Log.d(TAG, document.getData().get("createdAt").toString());

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
