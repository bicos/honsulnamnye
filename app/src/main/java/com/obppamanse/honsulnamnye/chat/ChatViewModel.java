package com.obppamanse.honsulnamnye.chat;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.obppamanse.honsulnamnye.util.ActivityUtils;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatViewModel extends BaseObservable {

    private ChatContract.View view;

    private ChatContract.Model model;

    public ChatViewModel(ChatContract.View view, ChatContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public void setMessage(String message) {
        model.getChat().setMsg(message);
    }

    @Bindable
    public String getMessage() {
        return model.getChat().getMsg();
    }

    @Bindable
    public ChatContract.Model getModel() {
        return model;
    }

    public void clickInputChat(Context context) {
        model.requestInputChat(ActivityUtils.getActivity(context), new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                view.clearInputChat();
            }
        }, new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                view.clearInputChat();
                view.showErrorToast(e);
            }
        });
    }

    public boolean inputChatEditorAction(TextView tv, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND && event.getAction() == KeyEvent.ACTION_UP) {
            clickInputChat(tv.getContext());
            return true;
        }
        return false;
    }

    @BindingAdapter("setRecyclerView")
    public static void setRecyclerView(RecyclerView recyclerView, ChatViewModel viewModel) {
        if (recyclerView.getAdapter() == null) {
            ChatRecyclerAdapter adapter = new ChatRecyclerAdapter(viewModel.getModel().getChatRef());
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(adapter);
        }
    }
}
