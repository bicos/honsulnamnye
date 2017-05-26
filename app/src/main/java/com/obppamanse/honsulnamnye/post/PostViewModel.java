package com.obppamanse.honsulnamnye.post;

import android.databinding.BaseObservable;
import android.text.TextUtils;

/**
 * Created by raehyeong.park on 2017. 5. 26..
 */

public class PostViewModel extends BaseObservable {

    private PostContract.View view;

    private PostContract.Model model;

    public PostViewModel(PostContract.View view, PostContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public void updateTitle(CharSequence title) {

    }
}
