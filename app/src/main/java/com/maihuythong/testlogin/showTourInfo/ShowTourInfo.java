package com.maihuythong.testlogin.showTourInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.Polyline;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.firebase.MessagingTour;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.rate_comment_review.CommentAdapter;
import com.maihuythong.testlogin.rate_comment_review.GetPointOfTour;
import com.maihuythong.testlogin.rate_comment_review.SendCommentTour;
import com.maihuythong.testlogin.rate_comment_review.SendReviewTour;
import com.maihuythong.testlogin.showListStopPoints.showListStopPointsActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowTourInfo extends AppCompatActivity {

    private SharedPreferences sf;
    private String token;
    private long tourId;
    private TextView hostName;
    private TextView date;
    private TextView adult;
    private TextView child;
    private TextView price;

    private Button member;
    private Button review;
    private Button comment;
    private Button chat;
    private Button reviewStopPoint;
    private LinearLayout linearLayoutComment;
    private LinearLayout linearLayoutReview;
    private ImageButton sendComment;
    private EditText commentContent;
    private RatingBar ratingBar;
    private EditText reviewContent;
    RatingReviews ratingReviews;
    private TextView averageStar;
    private RatingBar smallRating;
    private TextView totalRating;

    private ArrayList<Member> listMembers;
    private ArrayList<Comment> listComments;

    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tour_info);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        Intent intent = getIntent();
        tourId = intent.getLongExtra("id", 1);

        hostName = findViewById(R.id.tour_info_host_name);
        date = findViewById(R.id.tour_info_day);
        adult = findViewById(R.id.tour_info_adult);
        child = findViewById(R.id.tour_info_child);
        price = findViewById(R.id.tour_info_price);
        member = findViewById(R.id.tour_info_member);
        review = findViewById(R.id.tour_info_review);
        comment = findViewById(R.id.tour_info_comment);
        listView = findViewById(R.id.tour_info_list_view);
        linearLayoutComment = findViewById(R.id.tour_info_linear_layout_send_comment);
        sendComment = findViewById(R.id.send_comment_tour_info);
        commentContent = findViewById(R.id.tour_info_comment_content);
        linearLayoutReview = findViewById(R.id.tour_info_linear_layout_review);
        ratingBar = findViewById(R.id.ratingBar_tour_info);
        reviewContent = findViewById(R.id.review_content_tour_info);
        ratingReviews = findViewById(R.id.rating_reviews);
        averageStar = findViewById(R.id.average_star);
        smallRating = findViewById(R.id.small_rating_bar);
        totalRating = findViewById(R.id.total_rating);
        chat = findViewById(R.id.tour_chat);
        reviewStopPoint= findViewById(R.id.stop_points_acc_tour_button);

        token = LoginActivity.token;
        if(token == null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        //get comment of tour
        APIService service = ApiUtils.getAPIService();
        service.getTourInfo(token, tourId).enqueue(new Callback<GetTourInfo>() {

            @Override
            public void onResponse(Call<GetTourInfo> call, Response<GetTourInfo> response) {
                if(response.code() == 200) {
                    GetTourInfo tourInfo = response.body();

                    listComments = tourInfo.getComments();
                    listMembers = tourInfo.getMembers();
                    String hostNameTour = getHostName(tourInfo.getMembers());
                    String startDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(tourInfo.getStartDate()));
                    String endDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(tourInfo.getEndDate()));


                    date.setText(startDateString + " - " + endDateString);
                    hostName.setText(hostNameTour);
                    if (tourInfo.getAdults() != 0){
                        adult.setText(String.valueOf(tourInfo.getAdults()));
                    }else {
                        adult.setText("0");
                    }

                    if (tourInfo.getChilds() != 0){
                        child.setText(String.valueOf(tourInfo.getChilds()));
                    }else {
                        child.setText("0");
                    }

                    price.setText(tourInfo.getMinCost() + " - " + tourInfo.getMaxCost());

                    member.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClickMember();
                        }
                    });

                    comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClickComment();
                        }
                    });

                    review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClickReview();
                        }
                    });

                    reviewStopPoint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ShowTourInfo.this, showListStopPointsActivity.class);
                            intent.putExtra("tourId",tourId);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetTourInfo> call, Throwable t) {

            }
        } );


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCommentTour();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
    }

    private void openChat() {
        Intent intent = new Intent(ShowTourInfo.this, MessagingTour.class);
        intent.putExtra("tourId", tourId);
        startActivity(intent);
    }

    private String getHostName(ArrayList<Member> members){
        for (Member m : members){
            if (m.isHost()){
                return m.getName();
            }
        }
        return "";
    }

    private void ClickMember(){
        linearLayoutComment.setVisibility(View.GONE);
        linearLayoutReview.setVisibility(View.GONE);
        MemberAdapter adapter =
                new MemberAdapter(this, R.layout.custom_tour_member, listMembers);
        listView.setAdapter(adapter);

        listView.getLayoutParams().height = 220*listMembers.size();

    }

    private void ClickComment(){
        linearLayoutComment.setVisibility(View.VISIBLE);
        linearLayoutReview.setVisibility(View.GONE);
        Collections.reverse(listComments);
        CommentAdapter adapter =
                new CommentAdapter(this, R.layout.custom_comment, listComments);
        listView.setAdapter(adapter);

        listView.getLayoutParams().height = 180*listComments.size();
    }

    private void ClickReview(){
        linearLayoutComment.setVisibility(View.GONE);
        linearLayoutReview.setVisibility(View.VISIBLE);
        listView.setAdapter(null);

        setRatingReviews();

        ImageButton sendReview = findViewById(R.id.send_review_tour_info);
        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPreviewTour();
            }
        });
    }

    private void SendCommentTour(){

        hideInputKeyboard();
        APIService mAPIService = ApiUtils.getAPIService();
        String comment = commentContent.getText().toString();
        if (comment == null || comment == ""){
            Toast.makeText(getApplicationContext(),"Please type your comment before send!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAPIService.sendCommentTour(token,(int)tourId,69, comment).enqueue(new Callback<SendCommentTour>() {
            @Override
            public void onResponse(Call<SendCommentTour> call, Response<SendCommentTour> response) {
                Comment comment1 = new Comment(69,"You", commentContent.getText().toString(),null,System.currentTimeMillis());
                listComments.add(0,comment1);
                listView.getLayoutParams().height = 180*listComments.size();
                //listViewComment.setEnabled(false);
                CommentAdapter adapterComment =
                        new CommentAdapter(ShowTourInfo.this, R.layout.custom_comment, listComments);
                listView.setAdapter(adapterComment);
                commentContent.setText("");
                Toast.makeText(getApplicationContext(),"Sent your comment", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SendCommentTour> call, Throwable t) {

            }
        });
    }

    private void SendPreviewTour(){
        hideInputKeyboard();
        APIService mAPIService = ApiUtils.getAPIService();
        int numStar = (int)ratingBar.getRating();
        if(numStar == 0){

            Toast.makeText(getApplicationContext(),"Please choose your star rating!",Toast.LENGTH_SHORT).show();
            return;
        }
        mAPIService.sendReview(token, tourId, numStar, reviewContent.getText().toString()).enqueue(new Callback<SendReviewTour>() {
            @Override
            public void onResponse(Call<SendReviewTour> call, Response<SendReviewTour> response) {
                reviewContent.setText("");
                Toast.makeText(getApplicationContext(),"Sent your review", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SendReviewTour> call, Throwable t) {

            }
        });
    }

    private void setRatingReviews(){
        final int colors[] = new int[]{
                Color.parseColor("#0e9d58"),
                Color.parseColor("#bfd047"),
                Color.parseColor("#ffc105"),
                Color.parseColor("#ef7e14"),
                Color.parseColor("#d36259")};

        // add data from api


        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getPointOfTour(token, tourId).enqueue(new Callback<GetPointOfTour>() {
            @Override
            public void onResponse(Call<GetPointOfTour> call, Response<GetPointOfTour> response) {
                if (response.code() == 200){
                    //GetPointOfTour res[] = response.body().getServicedId();
                    GetPointOfTour getPointOfTour = response.body();

                    GetPointOfTour.Point point[] = getPointOfTour.getPointStats();
                    final int raters[] = {point[0].getTotal(), point[1].getTotal(), point[2].getTotal(),
                            point[3].getTotal(), point[4].getTotal()};

                    double total = 0.0;
                    for (double t : raters){
                        total+= t;
                    }
                    total = total/5;
                    smallRating.setRating((float)total);

                    averageStar.setText(String.valueOf(total));

                    int count = 0;
                    for (GetPointOfTour.Point i : point){
                        count += i.getTotal();
                    }

                    totalRating.setText(String.valueOf(count));

                    ratingReviews.createRatingBars(100, BarLabels.STYPE1, colors, raters);
                }
            }

            @Override
            public void onFailure(Call<GetPointOfTour> call, Throwable t) {

            }
        });


    }

    void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception

        }
    }
}
