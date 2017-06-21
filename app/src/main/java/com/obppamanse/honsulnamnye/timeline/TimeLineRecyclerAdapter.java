package com.obppamanse.honsulnamnye.timeline;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
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

    public TimeLineRecyclerAdapter(Query q) {
        super(Post.class, R.layout.item_post, PostItemViewHolder.class, q);
    }

    @Override
    protected void populateViewHolder(PostItemViewHolder viewHolder, Post model, int position) {
        viewHolder.populateViewHolder(model);
    }
}
