package com.maihuythong.testlogin.showlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maihuythong.testlogin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            viewHolder.tvSrc = convertView.findViewById(R.id.tvSrc);
            viewHolder.tvDes = convertView.findViewById(R.id.tvDes);
            viewHolder.tvMinCost = convertView.findViewById(R.id.tvStartDate);
            viewHolder.tvMaxCost = convertView.findViewById(R.id.tvEndDate);
            viewHolder.tvAdult = convertView.findViewById(R.id.tvAdult);
            viewHolder.tvChildren = convertView.findViewById(R.id.tvChildren);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Tour tour = arrTour.get(position);
        viewHolder.tvName.setText(tour.getName());

        if(tour.getStartDate() != null && !tour.getStartDate().equals("")){
            String tmpDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(Long.parseLong(tour.getStartDate())));

            if(tmpDate != null){
                viewHolder.tvSrc.setText(tmpDate);
            }
            else{
                viewHolder.tvSrc.setText("No Date");
            }
        }

        if(tour.getEndDate() != null && !tour.getEndDate().equals("")){
            String tmpDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(Long.parseLong(tour.getEndDate())));
            if(tmpDate != null){
                viewHolder.tvDes.setText(tmpDate);
            }
            else{
                viewHolder.tvDes.setText("No Date");
            }
        }

        viewHolder.tvMinCost.setText(tour.getMinCost());
        viewHolder.tvMaxCost.setText(tour.getMaxCost());
        viewHolder.tvAdult.setText(tour.getAdults() + "");
        viewHolder.tvChildren.setText(tour.getChilds() + "");
        return convertView;
    }

    public class ViewHolder {
        TextView tvName;
        TextView tvSrc;
        TextView tvDes;
        TextView tvMinCost;
        TextView tvMaxCost;
        TextView tvAdult;
        TextView tvChildren;
    }

    public Tour getItem(int position){
        return arrTour.get(position);
    }


}
