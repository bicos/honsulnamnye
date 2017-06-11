package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.obppamanse.honsulnamnye.post.PostContract;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostDetailViewModel extends BaseObservable {

    PostContract.DeleteView view;

    PostContract.DetailModel model;

    public PostDetailViewModel(PostContract.DeleteView view, PostContract.DetailModel model) {
        this.view = view;
        this.model = model;
    }

    @Bindable
    public String getTitle(){
        return model.getTitle();
    }

    @Bindable
    public String getDesc() {
        return model.getDesc();
    }

    public void clickDeletePost(Activity activity){
        try {
            model.deletePost(activity, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        view.successDeletePost();
                    } else {
                        view.failureDeletePost(task.getException());
                    }
                }
            });
        } catch (Exception e) {
            view.failureDeletePost(e);
        }
    }
}
