package com.example.healthpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthpass.models.LocationsModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LocationHistoryActivity extends AppCompatActivity {

    public static final String TAG = "LocationHistoryActivity";

    private FirebaseFirestore db;
    private RecyclerView mFirestoreList;

    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        Log.d(TAG, "onCreate");

        mFirestoreList = findViewById(R.id.firestore_list);


        //
        //      Get current FireAuth Instance
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //      Current DB Instance
        db = FirebaseFirestore.getInstance();

        //      Current User Instance
        FirebaseUser firebaseUser = auth.getCurrentUser();

        //      Get Current User ID
        final String userID = firebaseUser.getUid();
        Log.d(TAG,"userID: " + userID);

        // Query
        //Query query = db.collection("locations").whereEqualTo("id", userID).orderBy("dateTime", Query.Direction.DESCENDING);
        Query query = db.collection("locations").whereEqualTo("id", userID).orderBy("datetime", Query.Direction.DESCENDING) ;

        // Recycler Options
        FirestoreRecyclerOptions<LocationsModel> options = new FirestoreRecyclerOptions.Builder<LocationsModel>()
                .setQuery(query, LocationsModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<LocationsModel, LocationsViewHolder>(options) {
            @NonNull
            @Override
            public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_location_single, parent, false);
                return new LocationsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull LocationsViewHolder holder, int position, @NonNull LocationsModel model) {
                Log.d(TAG, "Model: " + model.toString());
                holder.list_date_time.setText(model.getDatetime());
                holder.list_lat.setText(model.getLat() +"");
                holder.list_lon.setText(model.getLon() + "");

            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }

    private class LocationsViewHolder extends RecyclerView.ViewHolder {

        private TextView list_date_time;
        private TextView list_lat;
        private TextView list_lon;

        public LocationsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_date_time = itemView.findViewById(R.id.list_date_time);
            list_lat = itemView.findViewById(R.id.list_lat);
            list_lon = itemView.findViewById(R.id.list_lon);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
