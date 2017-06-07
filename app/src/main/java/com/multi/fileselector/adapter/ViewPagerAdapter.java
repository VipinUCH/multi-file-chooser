package com.multi.fileselector.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.multi.fileselector.fragments.BaseFragment;

import java.util.List;

/**
 * Created by Vipin on 6/5/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<BaseFragment> fragments;

    public ViewPagerAdapter(Context context, FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(fragments.get(position).getTitle());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}

