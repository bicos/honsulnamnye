package com.obppamanse.honsulnamnye.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obppamanse.honsulnamnye.R;

/**
 * Created by Ravy on 2017. 5. 21..
 */

public class PersonalFragment extends Fragment {

    public static PersonalFragment newInstance() {

        Bundle args = new Bundle();

        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        TabLayout layout = view.findViewById(R.id.tab);

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        PersonalCalendarFragmentAdapter adapter = new PersonalCalendarFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        layout.setupWithViewPager(viewPager);

        return view;
    }

    private static class PersonalCalendarFragmentAdapter extends FragmentPagerAdapter {

        public PersonalCalendarFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return PersonalCalendarFragment.newInstance(PersonalCalendarFragment.TYPE_GROUP_CREATOR);
            } else {
                return PersonalCalendarFragment.newInstance(PersonalCalendarFragment.TYPE_GROUP_JOINER);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "내가 만든 그룹";
            } else {
                return "내가 가입한 그룹";
            }
        }
    }
}
