package com.example.healthpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthpass.R;
import com.example.healthpass.adapter.HospitalsAdapter;
import com.example.healthpass.models.hospital;

import org.parceler.Parcels;

public class HospitalDetailsActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvAddress;
    TextView tvCity;
    TextView tvState;
    TextView tvCountyName;
    TextView tvZipCode;
    TextView tvLicensedBeds;
    TextView tvICUBeds;
    TextView tvBedUtilization;
    TextView tvAverageVentilatorUsage;
    Button btnSeeLocation;
    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_details);

        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvLicensedBeds = findViewById(R.id.tvLicensedBeds);
        tvICUBeds = findViewById(R.id.tvICUBeds);
        tvBedUtilization = findViewById(R.id.tvBedUtilization);
        tvAverageVentilatorUsage = findViewById(R.id.tvAverageVentilatorUsage);
        btnSeeLocation = findViewById(R.id.btnSeeLocation);
        btnHome = findViewById(R.id.btnHome);

        final hospital h = Parcels.unwrap(getIntent().getParcelableExtra("hospital"));

        String address = h.getAddress() +", "+h.getCity() +", "+h.getState()+", "+h.getZipCode();

        int utilization;
        utilization = (int) (h.getBedUtilization() * 100);
        tvName.setText(h.getName());
        tvAddress.setText(address);
        tvLicensedBeds.setText(h.getLicensedBeds());
        tvICUBeds.setText(h.getICUBeds());
        tvBedUtilization.setText("% "+String.valueOf(utilization));
        tvAverageVentilatorUsage.setText(h.getAverageVentilatorUsage());

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HospitalDetailsActivity.this, ResourcesActivity.class);
                startActivity(intent);
            }
        });

        btnSeeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HospitalDetailsActivity.this, LocationHisotryMapsActivity.class);
                i.putExtra("latitude", h.getLat());
                i.putExtra("longitude", h.getLon());
                i.putExtra("marker", h.getName()+"\n"+h.getAddress());
                startActivity(i);
            }
        });

    }
}