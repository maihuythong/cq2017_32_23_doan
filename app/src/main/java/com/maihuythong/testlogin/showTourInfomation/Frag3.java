package com.maihuythong.testlogin.showTourInfomation;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.rate_comment_review.CommentAdapter;
import com.maihuythong.testlogin.rate_comment_review.SendCommentTour;
import com.maihuythong.testlogin.showTourInfo.ShowTourInfo;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag3 extends Fragment {
    private EditText commentContent;
    private ListView mListView;
    private View mView;

    private static final String LIST_COMMENT = "listComment";
    private static final String TOKEN = "token";
    private static final String TOUR_ID = "tourId";

    private ArrayList<Comment> mListComment;
    private String mToken;
    private long mTourId;

    private OnFragmentInteractionListener mListener;

    public Frag3() {
        // Required empty public constructor
    }

    public static Frag3 newInstance(ArrayList<Comment> listComment, String token, long tourId) {
        Frag3 fragment = new Frag3();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LIST_COMMENT, listComment);
        args.putString(TOKEN, token);
        args.putLong(TOUR_ID, tourId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListComment = getArguments().getParcelableArrayList(LIST_COMMENT);
            mToken = getArguments().getString(TOKEN);
            mTourId = getArguments().getLong(TOUR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag3, container, false);

        Collections.reverse(mListComment);
        CommentAdapter adapter =
                new CommentAdapter(mView.getContext(), R.layout.custom_comment, mListComment);
        mListView = mView.findViewById(R.id.tour_info_list_view);
        mListView.setAdapter(adapter);
        mListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });

        commentContent = mView.findViewById(R.id.tour_info_comment_content);
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
        String comment = commentContent.getText().toString();

        if (comment == null || comment == ""){
            Toast.makeText(getApplicationContext(),"Please type your comment before send!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAPIService.sendCommentTour(mToken,(int)mTourId,69, comment).enqueue(new Callback<SendCommentTour>() {
            @Override
            public void onResponse(Call<SendCommentTour> call, Response<SendCommentTour> response) {
                Comment comment1 = new Comment(69,"You", commentContent.getText().toString(),null,System.currentTimeMillis());
                mListComment.add(0,comment1);
                //listViewComment.setEnabled(false);
                CommentAdapter adapterComment =
                        new CommentAdapter(mView.getContext(), R.layout.custom_comment, mListComment);
                mListView.setAdapter(adapterComment);
                commentContent.setText("");
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
