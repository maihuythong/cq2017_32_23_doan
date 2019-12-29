package com.maihuythong.testlogin.showTourInfomation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfo.MemberAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag1 extends Fragment {
    private String mHostName;
    private String mAdult;
    private String mChild;
    private String mDate;
    private String mPrice;
    private String mName;
    private ArrayList<Member> mListMembers;
    private View mView;
    private ListView mListView;

    private static final String LIST_MEMBERS = "listMembers";

    private OnFragmentInteractionListener mListener;

    public Frag1() {
        // Required empty public constructor
    }

    public static Frag1 newInstance(String hostName, String adult, String child, String date,
                                    String price, String name, ArrayList<Member> listMembers) {
        Frag1 fragment = new Frag1();
        Bundle args = new Bundle();
        args.putString("hostName", hostName);
        args.putString("adult", adult);
        args.putString("child", child);
        args.putString("date", date);
        args.putString("price", price);
        args.putString("name", name);
        args.putParcelableArrayList("listMembers",listMembers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHostName = getArguments().getString("hostName");
            mAdult = getArguments().getString("adult");
            mChild = getArguments().getString("child");
            mDate = getArguments().getString("date");
            mPrice = getArguments().getString("price");
            mName = getArguments().getString("name");
            mListMembers = getArguments().getParcelableArrayList("listMembers");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag1, container, false);

        TextView hostName = mView.findViewById(R.id.tour_info_host_name);
        hostName.setText(mHostName);

        TextView date = mView.findViewById(R.id.tour_info_day);
        date.setText(mDate);

        TextView adult = mView.findViewById(R.id.tour_info_adult);
        adult.setText(mAdult);

        TextView child = mView.findViewById(R.id.tour_info_child);
        child.setText(mChild);

        TextView price = mView.findViewById(R.id.tour_info_price);
        price.setText(mPrice);

        Collections.reverse(mListMembers);
        MemberAdapter adapter =
                new MemberAdapter(mView.getContext(), R.layout.custom_tour_member, mListMembers);
        mListView = mView.findViewById(R.id.tour_info_list_view);
        mListView.setOnTouchListener(new ListView.OnTouchListener() {
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
        mListView.setAdapter(adapter);

        ClickMember();

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

    private void ClickMember(){
        MemberAdapter adapter =
                new MemberAdapter(mView.getContext(), R.layout.custom_tour_member, mListMembers);
        mListView.setAdapter(adapter);
    }
}
