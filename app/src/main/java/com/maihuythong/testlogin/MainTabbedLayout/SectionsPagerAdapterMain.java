package com.maihuythong.testlogin.MainTabbedLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapterMain extends FragmentPagerAdapter {

    public SectionsPagerAdapterMain(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new Frag1Main();
                break;
            case 1:
                fragment = new Frag2Main();
                break;
            case 2:
                fragment = new Frag3Main();
                break;
            case 3:
                fragment = new Frag4Main();
                break;
            case 4:
                fragment = new Frag5Main();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "My Tours";
            case 1:
                return "Tours";
            case 2:
                return "Stop";
            case 3:
                return "Invite";
            case 4:
                return "Account";
        }
        return "Frag0";
    }

    @Override
    public int getCount() {
        return 5;
    }
}
