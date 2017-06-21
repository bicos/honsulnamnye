package com.obppamanse.honsulnamnye.timeline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseError;
import com.obppamanse.honsulnamnye.R;

/**
 * Created by Ravy on 2017. 5. 21..
 */

public class TimeLineFragment extends Fragment {

    private TimeLineRecyclerAdapter adapter;

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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.loading_progress);
        progressBar.show();

        adapter = new TimeLineRecyclerAdapter() {
            @Override
            protected void onDataChanged() {
                if (progressBar.isShown()) {
                    progressBar.hide();
                }
            }

            @Override
            protected void onCancelled(DatabaseError error) {
                if (progressBar.isShown()) {
                    progressBar.hide();
                }
            }
        };
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        adapter.cleanup();
        super.onDestroyView();
    }
}
