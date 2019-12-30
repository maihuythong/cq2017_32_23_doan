package com.maihuythong.testlogin.stopPointInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maihuythong.testlogin.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FeedbackUserAdapter extends RecyclerView.Adapter<FeedbackUserAdapter.ViewHolder> {

    private List<Feedback> arrFeedback;

    public FeedbackUserAdapter(List<Feedback> objects) {
        this.arrFeedback=objects;
    }


    @NonNull
    @Override
    public FeedbackUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackUserAdapter.ViewHolder holder, int position) {

        Feedback feedback = arrFeedback.get(position);

        String dateFormat= "dd/MM/yyyy";
        String createOn="";
        if(Objects.isNull(feedback.getCreateOn())) createOn="";
        else createOn = getDate(Long.valueOf(feedback.getCreateOn()),dateFormat);

        if (feedback.getAvatar() != null){
            Picasso.get().load(feedback.getAvatar()).into(holder.userAvatar);
        }else {
            holder.userAvatar = null;                                               // Add default avatar here
        }
        holder.userName.setText(feedback.getName());
        holder.ratingBar.setRating(feedback.getPoint());
        holder.createOn.setText(createOn);
        holder.feedback.setText(feedback.getFeedback());
    }

    @Override
    public int getItemCount() {
        return arrFeedback.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userAvatar;
        public TextView userName;
        public RatingBar ratingBar;
        public TextView createOn;
        public TextView feedback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userAvatar= (ImageView)itemView.findViewById(R.id.user_avatar_feedback);
            userName= (TextView) itemView.findViewById(R.id.name_user_feedback);
            ratingBar= (RatingBar) itemView.findViewById(R.id.small_rating_bar_user_feedback);
            createOn= (TextView) itemView.findViewById(R.id.feed_back_user_create_on);
            feedback= (TextView)itemView.findViewById(R.id.feed_back_of_user);
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
