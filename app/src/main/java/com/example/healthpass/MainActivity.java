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

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    final private String TAG = "MainActivity";
    Button buttonStartResourceActivity;
    Button buttonStartSurvey;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Button Declarations
        buttonStartResourceActivity = findViewById(R.id.button_resources);
        buttonStartSurvey = findViewById(R.id.button_start_survey);
        btnLogout =findViewById(R.id.logoutBtn);

//      Get Current User
        final FirebaseAuth auth = FirebaseAuth.getInstance();


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
    private void signOut(FirebaseAuth instance)
    {
//      Ends users session
        instance.signOut();
    }
}
