package com.maihuythong.testlogin.showListStopPoints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showTourInfo.StopPoint;

import java.util.List;

public class StopPointAdapter extends ArrayAdapter<StopPoint> {

    private Context context;
    private int resource;
    private List<StopPoint> arrStopPoint;
    public StopPointAdapter(@NonNull Context context, int resource, @NonNull List<StopPoint> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource=resource;
        this.arrStopPoint = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(this.resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleStopPoint= (TextView) convertView.findViewById(R.id.title_stop_point);
            viewHolder.addrStopPoint= (TextView) convertView.findViewById(R.id.addr_stop_point);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StopPoint stopPoint = arrStopPoint.get(position);
        viewHolder.titleStopPoint.setText(stopPoint.getName());
        viewHolder.addrStopPoint.setText(stopPoint.getAddress());
        return convertView;
    }

    public class ViewHolder {
        public TextView titleStopPoint;
        private TextView addrStopPoint;
    }

    public StopPoint getItem(int position){
        return arrStopPoint.get(position);
    }
}
