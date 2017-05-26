package com.obppamanse.honsulnamnye.post;

import android.app.Activity;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.model.Post;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class PostModel implements PostContract.Model {

    private DatabaseReference postRef;

    private Post post;

    public PostModel() {
        postRef = FirebaseUtils.getPostRef();
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getTitle() {
        return post != null ? post.getTitle() : null;
    }

    public String getDesc() {
        return post != null ? post.getDesc() : null;
    }

    public long getTime() {
        return post != null ? post.getTime() : 0L;
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

        postRef.child(key).setValue(post).addOnCompleteListener(activity, listener);
    }

    @Override
    public void modifyPost(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        if (post == null || TextUtils.isEmpty(post.getKey())) {
            throw new PostContract.FailureModifyPostException();
        }

        postRef.child(post.getKey()).setValue(post).addOnCompleteListener(activity, listener);
    }

    @Override
    public void deletePost(Activity activity, final OnCompleteListener<Void> listener) throws Exception {
        postRef.child(post.getKey()).removeValue().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    post = null;

                }
                listener.onComplete(task);
            }
        });
    }
}
