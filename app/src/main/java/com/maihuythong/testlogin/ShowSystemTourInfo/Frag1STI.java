package com.maihuythong.testlogin.ShowSystemTourInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfo.MemberAdapter;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag1STI.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag1STI#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag1STI extends Fragment {
    private SharedPreferences sf;
    RatingReviews ratingReviews;
    TextView tourName;
    TextView duration;
    TextView adults;
    TextView children;
    TextView price;
    TextView tourStatus;
    RatingBar ratingBar;
    EditText reviewContent;
    EditText commentTour;
    Button sendReview;
    Button sendComment;
    Button reviewStopPoint;
    ListView listViewComment;
    View mView;

    String token;
    long tourId;

    private String mTourName;
    private String mDuration;
    private long mAdults;
    private long mChildren;
    private String mPrice;
    private long mTourStatus;

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String ADULT = "adult";
    private final static String CHILD = "child";
    private final static String MAX_COST = "maxCost";
    private final static String MIN_COST = "minCost";
    private final static String START_DATE = "startDate";
    private final static String END_DATE = "endDate";
    private final static String TOUR_STATUS = "tourStatus";

    final ArrayList<Comment> arrComment = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public Frag1STI() {
        // Required empty public constructor
    }

    public static Frag1STI newInstance(long id, String name, long adult, long child,
                                       String maxCost, String minCost, long startDate,
                                       long endDate, long tourStatus) {
        Frag1STI fragment = new Frag1STI();
        Bundle args = new Bundle();
        args.putLong(ID, id);
        args.putString(NAME, name);
        args.putLong(ADULT, adult);
        args.putLong(CHILD, child);
        args.putLong(START_DATE, startDate);
        args.putLong(END_DATE, endDate);
        args.putString(maxCost, maxCost);
        args.putString(minCost, minCost);
        args.putLong(TOUR_STATUS, tourStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTourName = getArguments().getString(NAME);
            mAdults =  getArguments().getLong(ADULT);
            mChildren = getArguments().getLong(CHILD);
            mPrice = getArguments().getString(MIN_COST) + " - " +
                    getArguments().getString(MAX_COST);
            mTourStatus = getArguments().getLong(TOUR_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag1_sti, container, false);

        tourName = mView.findViewById(R.id.tour_name_rating);
        duration = mView.findViewById(R.id.tour_duration_rating);
        adults = mView.findViewById(R.id.tour_adult_rating);
        children = mView.findViewById(R.id.tour_child_rating);
        price = mView.findViewById(R.id.tour_price_rating);
        tourStatus = mView.findViewById(R.id.tour_info_status);

        tourName.setText(mTourName);
        adults.setText(mAdults + "");
        children.setText(mChildren + "");
        price.setText(mPrice);
        tourStatus.setText(mTourStatus == 0 ? "Pending start" : "Started");

        token = LoginActivity.token;
        if(token == null){
            sf = mView.getContext().getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
