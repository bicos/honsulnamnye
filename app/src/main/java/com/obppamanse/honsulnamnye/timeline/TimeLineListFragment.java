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
import com.google.firebase.database.Query;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.main.model.Category;

/**
 * Created by Ravy on 2017. 5. 21..
 */

public class TimeLineListFragment extends Fragment {

    public static final String PARAM_CATEGORY = "category";

    private Category category;

    private TimeLineRecyclerAdapter adapter;

    public static TimeLineListFragment newInstance(Category category) {

        Bundle args = new Bundle();
        args.putParcelable(PARAM_CATEGORY, category);

        TimeLineListFragment fragment = new TimeLineListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            category = bundle.getParcelable(PARAM_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ContentLoadingProgressBar progressBar = view.findViewById(R.id.loading_progress);
        progressBar.show();

        Query query = Category.ALL.equals(category.getCode()) ?
                FirebaseUtils.getPostRef() :
                FirebaseUtils.getPostRef().orderByChild("category").equalTo(category.getCode());

        adapter = new TimeLineRecyclerAdapter(query) {
            @Override
            public void onDataChanged() {
                if (progressBar.isShown()) {
                    progressBar.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
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
