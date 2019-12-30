package com.maihuythong.testlogin.TourCoordinate;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowSPInformation.ShowSPInformation;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
import com.maihuythong.testlogin.stopPointInfo.UpdateStopPointActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedSPAdapter extends RecyclerView.Adapter<SelectedSPAdapter.ViewHolder> {
    private ArrayList<StopPoint> notis;
    private Context context;
    private StopPoint n;
    private long mTourId;

    public SelectedSPAdapter(ArrayList<StopPoint> notis, long tourId) {
        this.notis = notis;
        this.mTourId =  tourId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_selected_stop_point, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        n = notis.get(position);

        TextView spName = holder.spName;
        spName.setText(n.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStopPoint();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView spName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spName = itemView.findViewById(R.id.stop_point_name);
            context = itemView.getContext();
        }
    }

    private void UpdateStopPoint(){
        Intent intent = new Intent(context, UpdateStopPointActivity.class);
        intent.putExtra("address",n.getAddress());
        intent.putExtra("provinceId",n.getProvinceId());
        intent.putExtra("name",n.getName());
        intent.putExtra("arrivalAt",n.getArrivalAt());
        intent.putExtra("lat",n.getLat());
        intent.putExtra("long",n.getLong());
        intent.putExtra("leaveAt",n.getLeaveAt());
        intent.putExtra("minCost",n.getMinCost());
        intent.putExtra("maxCost",n.getMaxCost());
        intent.putExtra("id",n.getId());
        intent.putExtra("serviceTypeId",n.getServiceTypeId());
        intent.putExtra("tourId", mTourId);
        context.startActivity(intent);
    }
}
