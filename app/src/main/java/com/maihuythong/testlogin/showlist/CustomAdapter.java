package com.maihuythong.testlogin.showlist;

import android.content.Context;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 8/27/2016.
 */
public class CustomAdapter extends ArrayAdapter<Tour> {

    private Context context;
    private int resource;
    private List<Tour> arrTour;

    public CustomAdapter(Context context, int resource, ArrayList<Tour> arrTour) {
        super(context, resource, arrTour);
        this.context = context;
        this.resource = resource;
        this.arrTour = arrTour;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(this.resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvId = (TextView) convertView.findViewById(R.id.tvId);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Tour tour = arrTour.get(position);
        viewHolder.tvName.setText("Tên chuyến đi: " + tour.getName());
        viewHolder.tvId.setText("Mã chuyến đi: " + tour.getID());
        return convertView;
    }

    public class ViewHolder {
        public TextView tvId;
        TextView tvName;

    }
}
