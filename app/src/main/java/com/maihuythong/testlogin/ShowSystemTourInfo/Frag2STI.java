package com.maihuythong.testlogin.ShowSystemTourInfo;

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

import com.google.android.material.snackbar.Snackbar;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.rate_comment_review.CommentAdapter;
import com.maihuythong.testlogin.rate_comment_review.GetCommentTour;
import com.maihuythong.testlogin.rate_comment_review.GetPointOfTour;
import com.maihuythong.testlogin.rate_comment_review.RateCommentTour;
import com.maihuythong.testlogin.rate_comment_review.SendReviewTour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.Math.round;

public class Frag2STI extends Fragment {
    private View mView;
    private RatingBar ratingBar;
    private EditText reviewContent;
    private RatingBar smallRating;
    private TextView averageStar;
    private TextView totalRating;

    RatingReviews ratingReviews;
    private String mToken;
    private long mTourId;

    private static final String TOKEN = "token";
    private static final String TOUR_ID = "tourId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag2_sti, container, false);
        ratingBar = mView.findViewById(R.id.ratingBar_tour_info);
        reviewContent = mView.findViewById(R.id.review_content_tour_info);
        smallRating = mView.findViewById(R.id.small_rating_bar);
        averageStar = mView.findViewById(R.id.average_star);
        totalRating = mView.findViewById(R.id.total_rating);
        ratingReviews = mView.findViewById(R.id.rating_reviews);

        mToken = LoginActivity.token;
        if(mToken == null){
            SharedPreferences sf = mView.getContext().getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            mToken = sf.getString("login_access_token", "");
        }

        ClickReview();

        return mView;
    }

    public static Frag2STI newInstance(long tourId) {
        Frag2STI fragment = new Frag2STI();
        Bundle args = new Bundle();
        args.putLong(TOUR_ID, tourId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTourId = getArguments().getLong(TOUR_ID);
        }
    }

    private void ClickReview() {
        setRatingReviews();

        ImageButton sendReview = mView.findViewById(R.id.send_review_tour_info);
        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPreviewTour();
            }
        });
    }

    private void SendPreviewTour() {
        hideInputKeyboard();
        APIService mAPIService = ApiUtils.getAPIService();
        int numStar = (int) ratingBar.getRating();
        if (numStar == 0) {

            Toast.makeText(mView.getContext(), "Please choose at least 1 star!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAPIService.sendReview(mToken, mTourId, numStar, reviewContent.getText().toString()).enqueue(new Callback<SendReviewTour>() {
            @Override
            public void onResponse(Call<SendReviewTour> call, Response<SendReviewTour> response) {
                reviewContent.setText("");
                Snackbar.make(mView, "Review sent", Snackbar.LENGTH_SHORT).show();
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

        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getPointOfTour(mToken, mTourId).enqueue(new Callback<GetPointOfTour>() {
            @Override
            public void onResponse(Call<GetPointOfTour> call, Response<GetPointOfTour> response) {
                if (response.code() == 200){
                    //GetPointOfTour res[] = response.body().getServicedId();
                    GetPointOfTour getPointOfTour = response.body();

                    GetPointOfTour.Point point[] = getPointOfTour.getPointStats();
                    final int raters[] = {point[4].getTotal(), point[3].getTotal(), point[2].getTotal(),
                            point[1].getTotal(), point[0].getTotal()};

                    int nRaters = 0;
                    for(int i = 0; i<5; i++){
                        nRaters += raters[i];
                    }

                    double total = 0.0;
                    for(int i = 4; i>=0; i--){
                        total += raters[i] * (4-i+1);
                    }
                    total = round(10*total/nRaters)/10.0;
                    smallRating.setRating((float)total);

                    averageStar.setText(String.valueOf(total));

                    int count = 0;
                    for (GetPointOfTour.Point i : point){
                        count += i.getTotal();
                    }

                    totalRating.setText(String.valueOf(count));

                    ratingReviews.createRatingBars(nRaters, BarLabels.STYPE1, colors, raters);
                }
            }

            @Override
            public void onFailure(Call<GetPointOfTour> call, Throwable t) {

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
