package com.example.healthpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

public class QRActivity extends AppCompatActivity {
    public static final String TAG = "QRActivity";
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

            }
        });
    }
}