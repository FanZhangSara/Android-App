package com.example.fanzhang.myapplication3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class fPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public fPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                finfoFragment tab1 = new finfoFragment();
                return tab1;
            case 1:
                fphotoFragment tab2 = new fphotoFragment();
                return tab2;
            case 2:
                fmapFragment tab3 = new fmapFragment();
                return tab3;
            case 3:
                freviewsFragment tab4 = new freviewsFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
