package com.yc.allbluetooth.huilu.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @Date 2024/7/26 9:03
 * @Author ZJY
 * @email 714694358@qq.com
 */
public class HLXtszAdapter extends FragmentPagerAdapter {

    private final List<String> titleLists;
    private final List<Fragment> fragments;
    public HLXtszAdapter(FragmentManager fm, List<String> mTitleList, List<Fragment> fragments) {
        super(fm);
        this.titleLists = mTitleList;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titleLists.get(position);
    }
}
