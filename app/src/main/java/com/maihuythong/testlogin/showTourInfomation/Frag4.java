package com.maihuythong.testlogin.showTourInfomation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.rate_comment_review.CommentAdapter;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfo.MemberAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class Frag4 extends Fragment {
    private ArrayList<Member> mListMembers;

    private static final String LIST_MEMBERS = "listMembers";
    private View mView;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListMembers = getArguments().getParcelableArrayList(LIST_MEMBERS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag4, container, false);

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

    public static Frag4 newInstance(ArrayList<Member> listMembers) {
        Frag4 fragment = new Frag4();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LIST_MEMBERS, listMembers);
        fragment.setArguments(args);
        return fragment;
    }

    private void ClickMember(){
        MemberAdapter adapter =
                new MemberAdapter(mView.getContext(), R.layout.custom_tour_member, mListMembers);
        mListView.setAdapter(adapter);
    }
}
