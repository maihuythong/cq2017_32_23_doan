package com.maihuythong.testlogin.rate_comment_review;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.ListUsersActivity;
import com.maihuythong.testlogin.ShowListUsers.UserReq;
import com.maihuythong.testlogin.showListStopPoints.showListStopPointsActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateCommentTour extends AppCompatActivity {

    private SharedPreferences sf;
    RatingReviews ratingReviews;
    TextView tourName;
    TextView duration;
    TextView adults;
    TextView children;
    TextView price;
    RatingBar ratingBar;
    EditText reviewContent;
    EditText commentTour;
    Button sendReview;
    Button sendComment;
    Button reviewStopPoint;

    ListView listViewComment;

    String token;
    long tourId;

    final ArrayList<Comment> arrComment = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_review_rating_comment);

        ratingBar = findViewById(R.id.ratingBar);
        reviewContent = findViewById(R.id.review_content);
        tourName = findViewById(R.id.tour_name_rating);
        duration = findViewById(R.id.tour_duration_rating);
        adults = findViewById(R.id.tour_adult_rating);
        children = findViewById(R.id.tour_child_rating);
        price = findViewById(R.id.tour_price_rating);
        commentTour = findViewById(R.id.comment_content);
        ratingReviews = findViewById(R.id.rating_reviews);
        sendReview = findViewById(R.id.send_review);
        sendComment = findViewById(R.id.send_comment_tour);
        listViewComment = findViewById(R.id.list_view_comment);
        reviewStopPoint = (Button) findViewById(R.id.stop_points_review);


        token = LoginActivity.token;
        if(token == null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }

        setData();
        setRatingReviews();
        //get comment of tour
        APIService getCommentService = ApiUtils.getAPIService();
        getCommentService.getCommentTour(token, tourId,1,20).enqueue(new Callback<GetCommentTour>() {
            @Override
            public void onResponse(Call<GetCommentTour> call, Response<GetCommentTour> response) {
                if(response.code() == 200) {
                    Comment cm[];
                    cm = response.body().getComments();

                    for (int i = cm.length - 1; i >= 0; i--) {
                        arrComment.add(cm[i]);
                    }


                    listViewComment.getLayoutParams().height = 180*arrComment.size();
                    //listViewComment.setEnabled(false);
                    CommentAdapter adapterComment =
                            new CommentAdapter(RateCommentTour.this, R.layout.custom_comment, arrComment);
                    listViewComment.setAdapter(adapterComment);

                }
            }

            @Override
            public void onFailure(Call<GetCommentTour> call, Throwable t) {

            }
        } );




        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPreviewTour();
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCommentTour();
            }
        });

        reviewStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RateCommentTour.this, showListStopPointsActivity.class);
                intent.putExtra("isAccTour",false);
                intent.putExtra("tourId",tourId);
                startActivity(intent);
            }
        });
    }

    private void setData(){
        Intent intent = getIntent();

        tourId = intent.getLongExtra("id", 1);

        String tName = intent.getStringExtra("name");
        tourName.setText(tName);

//        long startDateTour = Long.getLong(intent.getStringExtra("startDate"));
//        long endDateTour = Long.getLong(intent.getStringExtra("endDate"));
//
//        String startDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(startDateTour));
//        String endDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(endDateTour));
//
//        duration.setText(startDateString + " - " + endDateString);


        long adultsReceived = intent.getLongExtra("adult",0);
        if (adultsReceived == 0){
            adults.setText(adultsReceived + "adults");
        }else{
            adults.setText("0 adults");
        }


        long childrenReceived = intent.getLongExtra("child",0);
        if (childrenReceived == 0){
            children.setText(childrenReceived + "children");
        }else{
            children.setText("0 chilren");
        }

        String minCostReceived = intent.getStringExtra("minCost");
        String maxCostReceived = intent.getStringExtra("maxCost");

        price.setText(minCostReceived + " - " + maxCostReceived + "VND");
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
                    final int raters[] = {point[4].getTotal(), point[3].getTotal(), point[2].getTotal(),
                                            point[1].getTotal(), point[0].getTotal()};

                    ratingReviews.createRatingBars(100, BarLabels.STYPE1, colors, raters);
                }
            }

            @Override
            public void onFailure(Call<GetPointOfTour> call, Throwable t) {

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

    private void SendCommentTour(){

        hideInputKeyboard();
        APIService mAPIService = ApiUtils.getAPIService();
        String comment = commentTour.getText().toString();
        if (comment == null || comment == ""){
            Toast.makeText(getApplicationContext(),"Please type your comment before send!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAPIService.sendCommentTour(token,(int)tourId,69, comment).enqueue(new Callback<SendCommentTour>() {
            @Override
            public void onResponse(Call<SendCommentTour> call, Response<SendCommentTour> response) {
                Comment comment1 = new Comment(69,"You", commentTour.getText().toString(),null,System.currentTimeMillis());
                arrComment.add(0,comment1);
                listViewComment.getLayoutParams().height = 180*arrComment.size();
                //listViewComment.setEnabled(false);
                CommentAdapter adapterComment =
                        new CommentAdapter(RateCommentTour.this, R.layout.custom_comment, arrComment);
                listViewComment.setAdapter(adapterComment);
                commentTour.setText("");
                Toast.makeText(getApplicationContext(),"Sent your comment", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SendCommentTour> call, Throwable t) {

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
