package com.maihuythong.testlogin.ShowSystemTourInfo;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfomation.Frag1;
import com.maihuythong.testlogin.showTourInfomation.Frag2;
import com.maihuythong.testlogin.showTourInfomation.Frag3;
import com.maihuythong.testlogin.showTourInfomation.Frag5;
import com.maihuythong.testlogin.showTourInfomation.TourOverview;

import java.util.ArrayList;

public class SectionsPagerAdapterSTI extends FragmentPagerAdapter {
    private final Context mContext;
    long mId;
    String mName;
    long mAdult;
    long mChild;
    String mMaxCost;
    String mMinCost;
    long mStartDate;
    long mEndDate;
    long mTourStatus;

    public SectionsPagerAdapterSTI(Context context, FragmentManager fm,
                                   long id, String name, long adult, long child,
                                   String maxCost, String minCost, long startDate,
                                   long endDate, long tourStatus) {
        super(fm);
        mContext = context;
        mId = id;
        mName = name;
        mAdult = adult;
        mChild = child;
        mMaxCost = maxCost;
        mMinCost = minCost;
        mStartDate = startDate;
        mEndDate = endDate;
        mTourStatus = tourStatus;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = Frag1STI.newInstance(mId, mName, mAdult, mChild, mMaxCost,
                        mMinCost, mStartDate, mEndDate, mTourStatus);
                break;
            case 1:
                fragment = Frag2STI.newInstance(mId);
                break;
            case 2:
                fragment = Frag3STI.newInstance(mId);
                break;
            case 3:
                fragment = Frag5STI.newInstance(mId);
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
                return "Ratings";
            case 2:
                return "COMMENTS";
            case 3:
                return "STOPPOINT";
        }
        return "Frag0";
    }

    @Override
    public int getCount() {
        return 4;
    }
}
