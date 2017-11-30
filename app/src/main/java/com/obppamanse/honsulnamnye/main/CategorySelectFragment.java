package com.obppamanse.honsulnamnye.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obppamanse.honsulnamnye.R;

/**
 * Created by raehyeong.park on 2017. 11. 30..
 */

public class CategorySelectFragment extends Fragment {

    private CategorySelectRecyclerAdapter adapter;

    public static CategorySelectFragment newInstance() {

        Bundle args = new Bundle();

        CategorySelectFragment fragment = new CategorySelectFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public CategorySelectFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_category,container,false);

        adapter = new CategorySelectRecyclerAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.list_category);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
