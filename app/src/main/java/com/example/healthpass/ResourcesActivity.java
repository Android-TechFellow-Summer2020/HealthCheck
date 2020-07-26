package com.example.healthpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResourcesActivity extends AppCompatActivity {

    Button btnNearbyHospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        btnNearbyHospitals = findViewById(R.id.btnNearbyHospitals);

        btnNearbyHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResourcesActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
    }
}