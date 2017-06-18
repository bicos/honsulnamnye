package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
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
    public String getTitle() {
        return model.getTitle();
    }

    @Bindable
    public String getDesc() {
        return model.getDesc();
    }

    @Bindable
    public DatabaseReference getParticipantRef() {
        return FirebaseUtils.getParticipantListRef(model.getPostKey());
    }

    @Bindable
    public boolean getIsWriter() {
        return model.isWriter();
    }

    @Bindable
    public boolean getAlreadyJoinThisGroup() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return false;
        }

        DatabaseReference ref = getParticipantListRef();

        if (ref == null) {
            return false; // 참여자가 없는 상태
        }

        return ref.child(user.getUid()) != null;
    }

    @Bindable
    public DatabaseReference getParticipantListRef(){
        return FirebaseUtils.getParticipantListRef(model.getPostKey());
    }

    public void clickDeletePost(Activity activity) {
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

    public void clickJoinGroup(Context context) {
        try {
            model.joinGroup((Activity) context, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    view.successJoinGroup();
                    notifyChange();
                }
            });
        } catch (Exception e) {
            view.failureJoinGroup(e);
        }
    }

    @BindingAdapter("setParticipantList")
    public static void setParticipantList(RecyclerView recyclerView, DatabaseReference reference) {
        if (reference == null) {
            return;
        }

        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (adapter == null) {
            recyclerView.setAdapter(new ParticipantListAdapter(reference));
        }
    }
}
