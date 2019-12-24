package com.maihuythong.testlogin.showAccountTours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showlist.Tour;

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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        tour = arrTour.get(position);
        viewHolder.tvName.setText("Tên chuyến đi: " + tour.getName());

        return convertView;
    }

    public class ViewHolder {
        TextView tvName;
        Button editTour;
    }

    public Tour getItem(int position){
        return arrTour.get(position);
    }
}

