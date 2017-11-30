package com.obppamanse.honsulnamnye.timeline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.obppamanse.honsulnamnye.main.model.Category;

import java.util.List;

/**
 * Created by raehyeong.park on 2017. 11. 30..
 */

public class TimeLinePagerAdapter extends FragmentPagerAdapter {

    private List<Category> categoryList;

    public TimeLinePagerAdapter(FragmentManager fm, List<Category> categoryList) {
        super(fm);
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        return TimeLineListFragment.newInstance(categoryList.get(position));
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getName();
    }
}
