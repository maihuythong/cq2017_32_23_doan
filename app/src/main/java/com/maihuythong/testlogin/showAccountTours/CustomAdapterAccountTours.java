package com.maihuythong.testlogin.showAccountTours;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.updateTour.UpdateTour;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterAccountTours extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<Tour> arrTour;
    private Tour tour;

    public CustomAdapterAccountTours(Context context, int resource, ArrayList<Tour> arrTour) {
        super(context, resource, arrTour);
        this.context = context;
        this.resource = resource;
        this.arrTour = arrTour;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(this.resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvId = (TextView) convertView.findViewById(R.id.tvId);
            viewHolder.editTour = convertView.findViewById(R.id.edit_tour);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        tour = arrTour.get(position);
        viewHolder.tvName.setText("Tên chuyến đi: " + tour.getName());
        viewHolder.tvId.setText("Mã chuyến đi: " + tour.getID());

        viewHolder.editTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tour tt = new Tour();
                tt = arrTour.get(position);
                Intent intent = new Intent(context, UpdateTour.class);
                intent.putExtra("pos", position);
                intent.putExtra("id", tt.getID());
                intent.putExtra("status", tt.getStatus());
                intent.putExtra("name", tt.getName());
                intent.putExtra("minCost", tt.getMinCost());
                intent.putExtra("maxCost", tt.getMaxCost());
                intent.putExtra("startDate", tt.getStartDate());
                intent.putExtra("endDate", tt.getEndDate());
                intent.putExtra("adult", tt.getAdults());
                intent.putExtra("child", tt.getChilds());
                intent.putExtra("isPrivate", tt.getIsPrivate());
                intent.putExtra("avatar", tt.getAvatar());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView tvId;
        TextView tvName;
        Button editTour;
    }
}

