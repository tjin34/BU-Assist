package net.bucssa.buassist.Ui.Classmates.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> mDatas;
    String [] titles = new String[]{"话题","小组"};

    public FragmentAdapter(FragmentManager fm, List<Fragment> mDatas) {
        super(fm);
        this.mDatas = mDatas;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
