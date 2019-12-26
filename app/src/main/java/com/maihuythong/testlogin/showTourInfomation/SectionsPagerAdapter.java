package com.maihuythong.testlogin.showTourInfomation;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.maihuythong.testlogin.rate_comment_review.Comment;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private String mHostName;
    private String mAdult;
    private String mChild;
    private String mDate;
    private String mPrice;
    private String mName;
    private String mToken;
    private long mTourId;
    private ArrayList<Comment> mListComment;

    public SectionsPagerAdapter(Context context, FragmentManager fm,
                                TourOverview tourOverview, ArrayList<Comment> listComment,
                                String token, long tourId) {
        super(fm);
        mContext = context;
        mAdult = tourOverview.getAdult();
        mChild = tourOverview.getChild();
        mDate = tourOverview.getDate();
        mPrice = tourOverview.getPrice();
        mHostName = tourOverview.getHostName();
        mName = tourOverview.getName();
        mListComment = listComment;
        mToken = token;
        mTourId = tourId;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = Frag1.newInstance(mHostName, mAdult, mChild, mDate, mPrice, mName);
                break;
            case 2:
                fragment = Frag3.newInstance(mListComment, mToken, mTourId);
                break;
            case 1:
                fragment = new Frag2();
                break;
            case 3:
                fragment = new Frag4();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Overview";
            case 1:
                return "Rating";
            case 2:
                return "Comments";
            case 3:
                return "Members";
        }
        return "Frag0";
    }

    @Override
    public int getCount() {
        return 4;
    }
}
