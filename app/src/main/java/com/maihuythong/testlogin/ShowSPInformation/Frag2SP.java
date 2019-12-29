package com.maihuythong.testlogin.ShowSPInformation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.rate_comment_review.GetPointOfTour;
import com.maihuythong.testlogin.rate_comment_review.SendReviewTour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.stopPointInfo.Feedback;
import com.maihuythong.testlogin.stopPointInfo.FeedbackUserAdapter;
import com.maihuythong.testlogin.stopPointInfo.StopPointDetailActivity;
import com.maihuythong.testlogin.stopPointInfo.feedbackListRes;
import com.maihuythong.testlogin.userInfo.UpdateUserInfoRes;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.StrictMath.round;

public class Frag2SP extends Fragment {
    private View mView;
    private TextView averageStarView;
    private RatingBar smallRatingView;
    private TextView totalRatingView;
    private RatingReviews ratingReviewsView;
    private ImageButton sendFeedBackButton;
    private RatingBar ratingBar;
    private EditText feedbackContentView;
    private RecyclerView feedbackLv;
    private RecyclerView.LayoutManager layoutManager;

    private int mServiceId;
    private Feedback[] arrFeedback;

    private static final String SERVICE_ID = "serviceId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag2_sp, container, false);

        averageStarView = mView.findViewById(R.id.average_star_stop_point);
        smallRatingView = mView.findViewById(R.id.small_rating_bar_stop_point);
        totalRatingView = mView.findViewById(R.id.total_rating_stop_point);
        ratingReviewsView = mView.findViewById(R.id.rating_reviews_stop_point);
        sendFeedBackButton = mView.findViewById(R.id.send_feedback_stop_point);
        ratingBar = mView.findViewById(R.id.ratingBar_stop_point);
        feedbackContentView = mView.findViewById(R.id.feedback_content_stop_point);
        feedbackLv = mView.findViewById(R.id.feed_back_list_view);

        setRatingReviews();
        sendFeedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
        getFeedbackList();

        return mView;
    }

    public static Frag2SP newInstance(int serviceId) {
        Frag2SP fragment = new Frag2SP();
        Bundle args = new Bundle();
        args.putInt(SERVICE_ID, serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mServiceId = getArguments().getInt(SERVICE_ID);
        }
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
        mAPIService.getFeedbackPointOfService(token, mServiceId).enqueue(new Callback<GetPointOfTour>() {
            @Override
            public void onResponse(Call<GetPointOfTour> call, Response<GetPointOfTour> response) {

                if (response.code() == 200) {
                    GetPointOfTour getPointOfTour = response.body();
                    GetPointOfTour.Point point[] = getPointOfTour.getPointStats();
                    final int raters[] = {point[4].getTotal(), point[3].getTotal(), point[2].getTotal(),
                            point[1].getTotal(), point[0].getTotal()};


                    double totalPoint = 0;
                    int capacityPoint = 0;
                    for (int i = 0; i < point.length; i++) {
                        totalPoint = point[i].getPoint() * point[i].getTotal() + totalPoint;
                        capacityPoint = capacityPoint + point[i].getTotal();
                    }

                    if (capacityPoint > 0) {
                        smallRatingView.setRating((float) totalPoint / capacityPoint);
//                        averageStarView.setText(String.valueOf(totalPoint / capacityPoint));
                        averageStarView.setText(String.valueOf(round(10* totalPoint / capacityPoint)/10.0));

                        totalRatingView.setText(String.valueOf(capacityPoint));
                        ratingReviewsView.createRatingBars(100, BarLabels.STYPE1, colors, raters);
                    }
                }
                if (response.code() == 500) {
                    Snackbar.make(mView, "Server error on getting point statistic", Snackbar.LENGTH_SHORT).show();
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
            sf = mView.getContext().getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        return token;
    }

    private void sendFeedback(){

        hideInputKeyboard();
        APIService mAPIService = ApiUtils.getAPIService();

        int numStar = (int)ratingBar.getRating();
        if(numStar == 0){
            Snackbar.make(mView, "Please choose your star rating!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        String token = GetTokenLoginAccess();
        long svid = mServiceId;
        String feedback = feedbackContentView.getText().toString();
        if(feedback.isEmpty()) return;

        mAPIService.sendfeedback(token,svid,numStar,feedback).enqueue(new Callback<UpdateUserInfoRes>() {
            @Override
            public void onResponse(Call<UpdateUserInfoRes> call, Response<UpdateUserInfoRes> response) {

                if(response.code()==200) {
                    Snackbar.make(mView, response.body().getMessage(), Snackbar.LENGTH_SHORT).show();
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(getActivity().getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().overridePendingTransition(0, 0);
                }
                if(response.code()==404)
                    Snackbar.make(mView, "1 start is not available!, 2 starts or more start", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UpdateUserInfoRes> call, Throwable t) {
                Snackbar.make(mView, "Send feedback failed", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getFeedbackList(){

        String token = GetTokenLoginAccess();

        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getFeedBackList(token,mServiceId,1,100).enqueue(new Callback<feedbackListRes>() {
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
                    layoutManager = new LinearLayoutManager(mView.getContext());
                    feedbackLv.setLayoutManager(layoutManager);

                    FeedbackUserAdapter feedbackUserAdapter = new FeedbackUserAdapter(arrayFeedbacks);
                    feedbackLv.setAdapter(feedbackUserAdapter);

                    Snackbar.make(mView, "List feedbacks was got success!", Snackbar.LENGTH_SHORT).show();
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
                    Snackbar.make(mView, "Error: " + message, Snackbar.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<feedbackListRes> call, Throwable t) {

            }
        });

    }

    void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)mView.getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception

        }
    }
}
