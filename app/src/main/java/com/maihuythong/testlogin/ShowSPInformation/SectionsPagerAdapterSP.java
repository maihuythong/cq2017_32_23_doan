package com.maihuythong.testlogin.ShowSPInformation;

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

public class SectionsPagerAdapterSP extends FragmentPagerAdapter {
    private final Context mContext;
    private SPOverview mSpOverview;


    public SectionsPagerAdapterSP(Context context, FragmentManager fm,
                                  SPOverview spOverview) {
        super(fm);
        mContext = context;
        mSpOverview = spOverview;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = Frag1SP.newInstance(mSpOverview);
                break;
            case 1:
                fragment = Frag2SP.newInstance(mSpOverview.getServiceId());
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
                return "RATING";
        }
        return "Frag0";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
