package com.obppamanse.honsulnamnye.post.write;

import android.app.Activity;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class PostWriteModel implements PostContract.WriteModel {

    private DatabaseReference postRef;

    private Post post;

    public PostWriteModel() {
        postRef = FirebaseUtils.getPostRef();
        post = new Post();
    }

    @Override
    public void writePost(Activity activity, OnCompleteListener<Void> listener) throws Exception{
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            throw new PostContract.NotExistAuthUserException();
        }

        if (post == null) {
            throw new PostContract.FailureWritePostException();
        }

        post.setUid(user.getUid());

        String key = postRef.push().getKey();
        if (TextUtils.isEmpty(key)) {
            throw new PostContract.FailureWritePostException();
        }

        post.setKey(key);
        post.setWriteTime(System.currentTimeMillis());

        postRef.child(key).setValue(post).addOnCompleteListener(activity, listener);
    }

    @Override
    public String getPlaceName() {
        return post != null && post.getPlace() != null ? post.getPlace().getName() : null;
    }

    @Override
    public long getDueDate() {
        return post != null ? post.getDueDateTime() : 0L;
    }

    @Override
    public void setTitle(String title) {
        if (post != null) {
            post.setTitle(title);
        }
    }

    @Override
    public void setDesc(String desc) {
        if (post != null) {
            post.setDesc(desc);
        }
    }

    @Override
    public void setPlace(Place place) {
        if (post != null) {
            post.setPlace(place);
        }
    }

    @Override
    public void setDueDate(long timeMill) {
        post.setDueDateTime(timeMill);
    }
}
