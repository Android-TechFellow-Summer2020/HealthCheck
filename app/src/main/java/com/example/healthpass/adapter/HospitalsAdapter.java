package com.example.healthpass.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthpass.HospitalDetailsActivity;
import com.example.healthpass.R;
import com.example.healthpass.models.hospital;

import org.parceler.Parcels;

import java.util.List;

public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.ViewHolder> {
    Context context;
    List<hospital> hospitalList;

    public HospitalsAdapter(Context context, List<hospital> hospitalList)
    {
        this.context = context;
        this.hospitalList = hospitalList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View hospitalView = LayoutInflater.from(context).inflate(R.layout.item_view_holder, parent, false);
        return new ViewHolder(hospitalView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        hospital h = hospitalList.get(position);
        holder.bind(h);

    }

    @Override
    public int getItemCount() {

        return hospitalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvHospitalName;
        TextView tvCity;
        TextView tvZipCode;
        ConstraintLayout clContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvZipCode = itemView.findViewById(R.id.tvZipCode);
            clContainer = itemView.findViewById(R.id.clContainer);
        }

        public void bind(final hospital h) {
            tvHospitalName.setText("Hospital Name: "+h.getName());
            tvCity.setText("City Name: "+h.getCity());
            tvZipCode.setText("Zip Code: "+h.getZipCode());
            clContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, HospitalDetailsActivity.class);
                    i.putExtra("hospital", Parcels.wrap(h));
                    context.startActivity(i);

                }
            });
        }
    }
}
