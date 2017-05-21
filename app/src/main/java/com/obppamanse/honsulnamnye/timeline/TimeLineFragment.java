package com.obppamanse.honsulnamnye.timeline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obppamanse.honsulnamnye.R;

/**
 * Created by Ravy on 2017. 5. 21..
 */

public class TimeLineFragment extends Fragment {

    public static TimeLineFragment newInstance() {

        Bundle args = new Bundle();

        TimeLineFragment fragment = new TimeLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        return view;
    }
}
