package com.obppamanse.honsulnamnye.timeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.model.Post;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class TimeLineRecyclerAdapter extends FirebaseRecyclerAdapter<Post, PostItemViewHolder> {

    public TimeLineRecyclerAdapter() {
        super(Post.class, R.layout.item_post, PostItemViewHolder.class, FirebaseUtils.getPostRef());
    }

    @Override
    protected void populateViewHolder(PostItemViewHolder viewHolder, Post model, int position) {
        viewHolder.populateViewHolder(model);
    }
}
