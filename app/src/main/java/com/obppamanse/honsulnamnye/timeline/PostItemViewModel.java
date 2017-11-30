package com.obppamanse.honsulnamnye.timeline;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.obppamanse.honsulnamnye.post.detail.PostDetailActivity;
import com.obppamanse.honsulnamnye.post.model.Participant;
import com.obppamanse.honsulnamnye.post.model.Post;
import com.obppamanse.honsulnamnye.util.DateUtils;

import java.util.Map;

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

    @Bindable
    public String getDate() {
        return post != null && post.getDueDateTime() != 0L ? DateUtils.getDateStr(post.getDueDateTime()) : "날짜 미정";
    }

    @Bindable
    public String getPlaceName() {
        return post != null && post.getPlace() != null && !TextUtils.isEmpty(post.getPlace().getName()) ?
                post.getPlace().getName() :
                "장소 미정";
    }

    @Bindable
    public String getParticipantsDesc(){
        if (post == null || post.getParticipantList() == null) {
            return "0명 참여중";
        }

        Map<String, Participant> participantMap = post.getParticipantList();
        return participantMap.keySet().size() + "명 참여중";
    }

    public void clickListItem(View view) {
        PostDetailActivity.start(view.getContext(), post.getKey());
    }
}
