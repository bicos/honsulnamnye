package com.obppamanse.honsulnamnye.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by raehyeong.park on 2017. 5. 26..
 */

public class PostWriteFragment extends Fragment {

    public PostWriteFragment() {
    }

    public static PostWriteFragment newInstance() {

        Bundle args = new Bundle();

        PostWriteFragment fragment = new PostWriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
