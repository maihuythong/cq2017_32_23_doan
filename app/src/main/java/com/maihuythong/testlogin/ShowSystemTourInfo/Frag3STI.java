package com.maihuythong.testlogin.ShowSystemTourInfo;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.rate_comment_review.CommentAdapter;
import com.maihuythong.testlogin.rate_comment_review.GetCommentTour;
import com.maihuythong.testlogin.rate_comment_review.RateCommentTour;
import com.maihuythong.testlogin.rate_comment_review.SendCommentTour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag3STI.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag3STI#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag3STI extends Fragment {
    private View mView;
    private SharedPreferences sf;
    Button sendComment;
    ListView listViewComment;
    EditText commentTour;

    final ArrayList<Comment> arrComment = new ArrayList<>();
    String token;
    long tourId;

    private OnFragmentInteractionListener mListener;

    public Frag3STI() {
        // Required empty public constructor
    }

    public static Frag3STI newInstance(long tourId) {
        Frag3STI fragment = new Frag3STI();
        Bundle args = new Bundle();
        args.putLong("tourId", tourId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tourId = getArguments().getLong("tourId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag3_sti, container, false);
        sendComment = mView.findViewById(R.id.send_comment_tour);
        listViewComment = mView.findViewById(R.id.tour_info_list_view);
        commentTour = mView.findViewById(R.id.comment_content);

        token = LoginActivity.token;
        if(token == null){
            sf = mView.getContext().getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }

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
                            new CommentAdapter(mView.getContext(), R.layout.custom_comment, arrComment);
                    listViewComment.setAdapter(adapterComment);

                }
            }

            @Override
            public void onFailure(Call<GetCommentTour> call, Throwable t) {

            }
        } );

        listViewComment.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        commentTour = mView.findViewById(R.id.tour_info_comment_content);
        ImageButton sendComment = mView.findViewById(R.id.send_comment_tour_info);
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCommentTour();
            }
        });


        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
                //listViewComment.setEnabled(false);
                CommentAdapter adapterComment =
                        new CommentAdapter(mView.getContext(), R.layout.custom_comment, arrComment);
                listViewComment.setAdapter(adapterComment);
                commentTour.setText("");
            }

            @Override
            public void onFailure(Call<SendCommentTour> call, Throwable t) {

            }
        });
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

    void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)mView.getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception

        }
    }
}
