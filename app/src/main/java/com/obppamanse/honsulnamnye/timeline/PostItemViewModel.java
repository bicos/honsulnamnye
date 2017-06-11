package com.obppamanse.honsulnamnye.timeline;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.obppamanse.honsulnamnye.post.model.Post;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostItemViewModel extends BaseObservable {

    private Post post;

    public PostItemViewModel() {
    }

    public void setPost(Post post) {
        this.post = post;
        notifyChange();
    }

    @Bindable
    public String getTitle() {
        return post != null ? post.getTitle() : null;
    }

    @Bindable
    public String getDesc() {
        return post != null ? post.getDesc() : null;
    }

    public void clickListItem(View view) {

    }
}
