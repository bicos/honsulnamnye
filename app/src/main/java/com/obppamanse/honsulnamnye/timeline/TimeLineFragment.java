package com.obppamanse.honsulnamnye.timeline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.main.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raehyeong.park on 2017. 11. 30..
 */

public class TimeLineFragment extends Fragment implements ValueEventListener {

    public static final String PARAM_CATEGORY = "category";

    private List<Category> categoryList;

    private Category selectCategory;

    private TabLayout categoryTab;

    private ViewPager categoryPager;

    private TimeLinePagerAdapter adapter;

    public static TimeLineFragment newInstance(Category category) {

        Bundle args = new Bundle();
        if (category != null) {
            args.putParcelable(PARAM_CATEGORY, category);
        }
        TimeLineFragment fragment = new TimeLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TimeLineFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectCategory = bundle.getParcelable(PARAM_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        categoryTab = view.findViewById(R.id.tab_category);
        categoryPager = view.findViewById(R.id.tab_pager);
        categoryTab.setupWithViewPager(categoryPager, true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUtils.getCategoryRef().addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            categoryList = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Category category = snapshot.getValue(Category.class);
                if (category == null) {
                    continue;
                }

                if (selectCategory == null) {
                    selectCategory = category;
                }

                categoryList.add(category);
            }
            adapter = new TimeLinePagerAdapter(getFragmentManager(), categoryList);
            categoryPager.setAdapter(adapter);

            categoryPager.setCurrentItem(getCurrentIndex(selectCategory));
        }
    }

    private int getCurrentIndex(Category category){
        if (category == null) {
            return 0;
        }

        for (int i = 0; i < categoryList.size(); i++) {
            if (category.getCode().equalsIgnoreCase(categoryList.get(i).getCode())) {
                return i;
            }
        }

        return 0;
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // do nothing
    }
}
