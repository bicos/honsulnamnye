package com.obppamanse.honsulnamnye.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.timeline.TimeLineRecyclerAdapter;

/**
 * Created by Ravy on 2017. 5. 21..
 */

public class PersonalFragment extends Fragment {


    private TimeLineRecyclerAdapter adapter;

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.loading_progress);
            progressBar.show();

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.personal_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            Query q = FirebaseUtils.getPostRef().orderByChild("uid").equalTo(user.getUid());
            adapter = new TimeLineRecyclerAdapter(q) {
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
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        if (adapter != null) {
            adapter.cleanup();
        }
        super.onDestroyView();
    }
}
