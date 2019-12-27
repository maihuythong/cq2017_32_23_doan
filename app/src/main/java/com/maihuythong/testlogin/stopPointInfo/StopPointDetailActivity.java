package com.maihuythong.testlogin.stopPointInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.rate_comment_review.GetPointOfTour;
import com.maihuythong.testlogin.showListStopPoints.showListStopPointsActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.UpdateUserInfoRes;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import com.maihuythong.testlogin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StopPointDetailActivity extends AppCompatActivity {

    private int id;
    private int serviceId;
    private String address;
    private int provinceId;
    private String name;
    private double Lat;
    private double Long;
    private long arrivalAt;
    private long leaveAt;
    private long minCost;
    private long maxCost;
    private int serviceTypeId;
    private String avatar;
    private Feedback[] arrFeedback;


    private TextView titleView;
    private TextView idView;
    private TextView serviceIdView;
    private TextView addressView;
    private TextView provinceIdView;
    private TextView dateView;
    private TextView priceView;
    private TextView serviceTypeView;
    private RatingReviews ratingReviewsView;
    private RatingBar ratingBar;
    private EditText feedbackContentView;
    private TextView averageStarView;
    private RatingBar smallRatingView;
    private TextView totalRatingView;
    private ImageButton sendFeedBackButton;
    private RecyclerView feedbackLv;
    private RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point_detail);

        titleView = findViewById(R.id.title_stop_point_info);
        idView = findViewById(R.id.stop_point_id);
        serviceIdView = findViewById(R.id.stop_point_serviceId);
        addressView = findViewById(R.id.stop_point_address);
        dateView = findViewById(R.id.stop_point_day);
        serviceTypeView = findViewById(R.id.stop_point_serviceType);
        provinceIdView = findViewById(R.id.stop_point_province);
        priceView = findViewById(R.id.stop_point_price);
        ratingReviewsView = findViewById(R.id.rating_reviews_stop_point);
        averageStarView = findViewById(R.id.average_star_stop_point);
        smallRatingView = findViewById(R.id.small_rating_bar_stop_point);
        totalRatingView = findViewById(R.id.total_rating_stop_point);
        feedbackContentView = findViewById(R.id.feedback_content_stop_point);
        ratingBar = findViewById(R.id.ratingBar_stop_point);
        sendFeedBackButton = findViewById(R.id.send_feedback_stop_point);
        feedbackContentView = findViewById(R.id.feedback_content_stop_point);
        feedbackLv = findViewById(R.id.feed_back_list_view);

        setData();
        setRatingReviews();

        sendFeedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        getFeedbackList();


    }

    private void setData(){
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        serviceId = intent.getIntExtra("serviceId",0);
        address = intent.getStringExtra("address");
        if(Objects.isNull(address)) address = "";
        provinceId = intent.getIntExtra("provinceId",0);
        name = intent.getStringExtra("name");
        if(Objects.isNull(name)) name = "";
        Lat = intent.getDoubleExtra("lat",0);
        Long = intent.getDoubleExtra("long",0);
        arrivalAt = intent.getLongExtra("arrivalAt",0);
        leaveAt = intent.getLongExtra("leaveAt",0);
        minCost = intent.getLongExtra("minCost",0);
        maxCost = intent.getLongExtra("maxCost",0);
        serviceTypeId = intent.getIntExtra("serviceTypeId",0);
        avatar = intent.getStringExtra("avatar");
        if(Objects.isNull(avatar)) avatar = "";


        String dateFormat= "dd/MM/yyyy";
        String dateArrival = getDate(arrivalAt,dateFormat);
        String dateLeave = getDate(leaveAt,dateFormat);
        String[] serviceArray;
        String[] provinceArray;
        serviceArray = getResources().getStringArray(R.array.service_array);
        provinceArray = getResources().getStringArray(R.array.province_array);


        titleView.setText(name);
        idView.setText(String.valueOf(id));
        serviceIdView.setText(String.valueOf(serviceId));
        addressView.setText(address);
        dateView.setText(dateArrival + " - " +dateLeave);
        serviceTypeView.setText(serviceArray[serviceTypeId-1]);
        if(provinceId<1||provinceId>63) provinceIdView.setText("");
        else provinceIdView.setText(provinceArray[provinceId-1]);
        priceView.setText(minCost + " - " + maxCost);


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


    private void setRatingReviews() {
        final int colors[] = new int[]{
                Color.parseColor("#0e9d58"),
                Color.parseColor("#bfd047"),
                Color.parseColor("#ffc105"),
                Color.parseColor("#ef7e14"),
                Color.parseColor("#d36259")};

        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getFeedbackPointOfService(token,serviceId).enqueue(new Callback<GetPointOfTour>() {
            @Override
            public void onResponse(Call<GetPointOfTour> call, Response<GetPointOfTour> response) {

                if(response.code()==200)
                {
                    GetPointOfTour getPointOfTour = response.body();
                    GetPointOfTour.Point point[] = getPointOfTour.getPointStats();
                    final int raters[] = {point[4].getTotal(), point[3].getTotal(), point[2].getTotal(),
                            point[1].getTotal(), point[0].getTotal()};


                    int totalPoint = 0;
                    int capacityPoint = 0;
                    for (int i =0;i<point.length;i++){
                       totalPoint = point[i].getPoint()*point[i].getTotal() + totalPoint;
                       capacityPoint=capacityPoint+point[i].getTotal();
                    }

                    if(capacityPoint>0) {
                        smallRatingView.setRating((float) totalPoint / capacityPoint);
                        averageStarView.setText(String.valueOf(totalPoint / capacityPoint));

                        totalRatingView.setText(String.valueOf(capacityPoint));
                        ratingReviewsView.createRatingBars(100, BarLabels.STYPE1, colors, raters);
                    }
                }
                if(response.code()==500)
                {
                    Toast.makeText(StopPointDetailActivity.this, "Server error on getting point statistic", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetPointOfTour> call, Throwable t) {

            }
        });




    }


    private String GetTokenLoginAccess(){
        SharedPreferences sf;
        String token;
        token = LoginActivity.token;
        if(token== null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        return token;
    }


    private void sendFeedback(){

        hideInputKeyboard();
        APIService mAPIService = ApiUtils.getAPIService();

        int numStar = (int)ratingBar.getRating();
        if(numStar == 0){
            Toast.makeText(getApplicationContext(),"Please choose your star rating!",Toast.LENGTH_SHORT).show();
            return;
        }
        String token = GetTokenLoginAccess();
        long svid = serviceId;
        String feedback = feedbackContentView.getText().toString();
        if(feedback.isEmpty()) return;

        mAPIService.sendfeedback(token,svid,numStar,feedback).enqueue(new Callback<UpdateUserInfoRes>() {
            @Override
            public void onResponse(Call<UpdateUserInfoRes> call, Response<UpdateUserInfoRes> response) {

                if(response.code()==200) {
                    Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0, 0);
                }
                if(response.code()==404)
                    Toast.makeText(getApplicationContext(),"1 start is not available!, 2 starts or more start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UpdateUserInfoRes> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Send feedback failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void getFeedbackList(){

        String token = GetTokenLoginAccess();

        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getFeedBackList(token,serviceId,1,100).enqueue(new Callback<feedbackListRes>() {
            @Override
            public void onResponse(Call<feedbackListRes> call, Response<feedbackListRes> response) {
                if(response.code()==200)
                {

                    arrFeedback = response.body().getFeedbacks();

                    final ArrayList<Feedback> arrayFeedbacks = new ArrayList<>();

                    for (int i=arrFeedback.length-1;i>=0;i--){
                        arrayFeedbacks.add(arrFeedback[i]);
                    }

                    feedbackLv.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    feedbackLv.setLayoutManager(layoutManager);

                    FeedbackUserAdapter feedbackUserAdapter = new FeedbackUserAdapter(arrayFeedbacks);
                    feedbackLv.setAdapter(feedbackUserAdapter);

                    Toast.makeText(getApplicationContext(),"List feedbacks was got success!", Toast.LENGTH_SHORT).show();
                }
                if(response.code()==500)
                {
                    String message = "Send failed!";
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        message = jObjError.getString("message");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    if(message.isEmpty()) message="Send failed!";
                    Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<feedbackListRes> call, Throwable t) {

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
