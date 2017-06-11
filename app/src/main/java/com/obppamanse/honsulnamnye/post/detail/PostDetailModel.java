package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Location;
import com.obppamanse.honsulnamnye.post.model.Post;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostDetailModel implements PostContract.DetailModel {

    private Post post;

    private DatabaseReference reference;

    public PostDetailModel(Post post) {
        this.reference = FirebaseUtils.getPostRef().child(post.getKey());
        this.post = post;
    }

    @Override
    public String getTitle() {
        return post != null ? post.getTitle() : null;
    }

    @Override
    public String getDesc() {
        return post != null ? post.getDesc() : null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public long getDueDate() {
        return post.getDueDateTime();
    }

    @Override
    public long getWriteDate() {
        return post.getWriteTime();
    }

    @Override
    public void deletePost(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        reference.removeValue().addOnCompleteListener(activity, listener);
    }
}
