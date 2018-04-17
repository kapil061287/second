package com.depex.okeyclick.user.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ServiceHistoryFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    FragmentManager fragmentManager;
    public ServiceHistoryFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentManager=fm;
        this.fragments=fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
