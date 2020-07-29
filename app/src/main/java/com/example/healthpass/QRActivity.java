package com.example.healthpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class QRActivity extends AppCompatActivity {
    public static final String TAG = "QRActivity";
    private static final int REQUEST_FINE_LOCATION = 200; //??
    //EditText etEnterText;

    Button btnGenerate;
    ImageView ivScannerImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String userID = firebaseUser.getUid();



        //tEnterText = findViewById(R.id.etEnterText);
        btnGenerate = findViewById(R.id.btnGenerate);
        ivScannerImage = findViewById(R.id.ivScannerImage);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("users").document(userID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            QRCodeWriter qrCodeWriter = new QRCodeWriter();
                            RelativeLayout Qr_layout = (RelativeLayout)findViewById(R.id.qr_layout);
                            boolean checker = (boolean)document.getData().get("pass");
                            if(checker ==false ){

                                Qr_layout.setBackgroundColor(Color.parseColor("#ff7c78"));
                            } else {
                                Qr_layout.setBackgroundColor(Color.parseColor("#7dff8e"));
                            }

                            try {
                                BitMatrix bitMatrix = qrCodeWriter.encode(document.getData().get("answers").toString(), BarcodeFormat.QR_CODE, 400,400);
                                Bitmap bitmap = Bitmap.createBitmap(400,400,Bitmap.Config.RGB_565);

                                for(int i = 0; i<400; i++)
                                {
                                    for(int j = 0; j<400; j++)
                                    {
                                        bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK : Color.WHITE);
                                    }
                                }

                                ivScannerImage.setImageBitmap(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

                saveLocation();
            }
        });
    }

    private void saveLocation() {

        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
        }
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions Not Granted ", Toast.LENGTH_SHORT).show();
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            Toast.makeText(getApplicationContext(), "Lat: " + location.getLatitude()+ " Lon: " + location.getLongitude() + "now: " + LocalDateTime.now().toString(), Toast.LENGTH_SHORT ).show();
                            saveLocationInDataStore(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    @SuppressLint("NewApi")
    private void saveLocationInDataStore(Location location) {


        //      Get current FireAuth Instance
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //      Current User Instance
        FirebaseUser firebaseUser = auth.getCurrentUser();

//      Current DB Instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

//      Get Current User ID
        final String userID = firebaseUser.getUid();

        //      HashMap with Answer information
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userID);
        map.put("lat", location.getLatitude());
        map.put("lon", location.getLongitude());

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        String dateTimeString =  dateTime.format(formatter);

        map.put("datetime", dateTimeString);

//      Add HashMap to answers DB
        db.collection("locations")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

//                      Add answer id to users answers array
                        FirebaseFirestore userDB = FirebaseFirestore.getInstance();
                        userDB.collection("users").document(userID).update("locations", FieldValue.arrayUnion(documentReference.getId()));

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