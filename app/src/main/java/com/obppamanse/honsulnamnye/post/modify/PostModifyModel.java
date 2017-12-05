package com.obppamanse.honsulnamnye.post.modify;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by raehyeong.park on 2017. 7. 11..
 */

public class PostModifyModel implements PostContract.ModifyModel {

    private DatabaseReference postRef;

    private Post post;

    private String tmpHashTag;

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
    public void setHashTag(String hashTag) {
        tmpHashTag = hashTag;
    }

    @Override
    public void setCategory(String categoryCode) {
        post.setCategory(categoryCode);
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
    public long getTimestamp() {
        return post.getTimestamp();
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
    public List<String> getFileNames() {
        return post != null ? post.getFileNames() : Collections.<String>emptyList();
    }

    @Override
    public List<String> getHashTagList() {
        return post.getHashTags();
    }

    @Override
    public String getCategory() {
        return post.getCategory();
    }

    @Override
    public Task<Void> modifyPost() {
        return postRef.setValue(post);
    }

    @Override
    public Task<Void> modifyUploadImage() {
        return postRef.child(FirebaseUtils.POST_FILENAMES_REF).setValue(post.getFileNames());
    }

    @Override
    public void removeFileName(String fileName) {
        Iterator<String> iterator = post.getFileNames().iterator();

        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals(fileName)) {
                iterator.remove();
            }
        }
    }

    @Override
    public String getHashTag() {
        return tmpHashTag;
    }
}
