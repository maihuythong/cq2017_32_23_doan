package com.maihuythong.testlogin.review;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.maihuythong.testlogin.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ReviewTourAdapter extends ArrayAdapter {

    private Context context;
    private int layoutInvite;
    private ArrayList<ReviewTour> data = new ArrayList<>();


    public ReviewTourAdapter(@NonNull Context context, int layoutInvite, ArrayList<ReviewTour> data) {
        super(context, layoutInvite, data);
        this.layoutInvite = layoutInvite;
        this.context = context;
        this.data = data;

    }

    class ViewHolder{
        TextView nameReview;
        TextView dateReview;
        TextView contentReview;
        ImageView imageMoreOption;
    }


    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ReviewTourAdapter.ViewHolder holder = null;
        final ReviewTourAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_layout_review_item, null);

            viewHolder = new ViewHolder();

            viewHolder.nameReview = convertView.findViewById(R.id.name_reviewer_tour);
            viewHolder.dateReview = convertView.findViewById(R.id.date_review_tour);
            viewHolder.contentReview = convertView.findViewById(R.id.text_review_tour);
            viewHolder.imageMoreOption = convertView.findViewById(R.id.review_tour_more_option);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            final ReviewTour reviewTour = data.get(position);

            viewHolder.nameReview.setText(reviewTour.getAuth());
            viewHolder.contentReview.setText(reviewTour.getReview());
            String dateString = new SimpleDateFormat("dd/MM/yyyy", Locale.TAIWAN).format(new Date(reviewTour.getDate()));
            viewHolder.dateReview.setText(dateString);

            viewHolder.imageMoreOption.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {



                    switch (v.getId()) {
                        case R.id.review_tour_more_option:

                            PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                            popup.getMenuInflater().inflate(R.menu.menu_tour_review,
                                    popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.report_tour:

                                            //Or Some other code you want to put here.. This is just an example.
                                            Toast.makeText(getApplicationContext(), " Report Clicked at position " + " : " + position, Toast.LENGTH_SHORT).show();

                                            break;
                                        default:
                                            break;
                                    }

                                    return true;
                                }
                            });

                            break;

                        default:
                            break;
                    }



                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        return convertView;
    }
}
