package com.obppamanse.honsulnamnye.post.modify;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;

/**
 * Created by raehyeong.park on 2017. 7. 11..
 */

public class PostModifyModel implements PostContract.ModifyModel {

    private DatabaseReference postRef;

    private Post post;

    public PostModifyModel(Post post) {
        this.postRef = FirebaseUtils.getPostRef().child(post.getKey());
        this.post = post;
    }

    @Override
    public void setTitle(String title) {
        post.setTitle(title);
    }

    @Override
    public void setDesc(String desc) {
        post.setDesc(desc);
    }

    @Override
    public void setPlace(Place place) {
        post.setPlace(place);
    }

    @Override
    public void setDueDate(long timeMillisecond) {
        post.setDueDateTime(timeMillisecond);
    }

    @Override
    public String getTitle() {
        return post.getTitle();
    }

    @Override
    public String getDesc() {
        return post.getDesc();
    }

    @Override
    public Place getPlace() {
        return post.getPlace();
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
    public boolean isWriter() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && user.getUid().equals(post.getUid());
    }

    @Override
    public String getPostKey() {
        return post.getKey();
    }

    @Override
    public void modifyPost(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        postRef.setValue(post).addOnCompleteListener(activity, listener);
    }
}
