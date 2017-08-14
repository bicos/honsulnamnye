package com.obppamanse.honsulnamnye.chat;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.chat.model.Chat;
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
        Activity activity = ActivityUtils.getActivity(context);
        if (activity == null) {
            return;
        }

        if (TextUtils.isEmpty(model.getChat().getMsg())) {
            view.showErrorToast(context.getString(R.string.error_input_text));
            return;
        }

        // copy of value
        Chat copiedChat = new Chat(model.getChat());

        // clear editText
        view.clearInputChat();

        model.requestUploadChat(activity, copiedChat).addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                view.moveScrollToPositionBottom();
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.showErrorToast(e);
            }
        });
    }

    public void clickUploadImage() {
        view.chooseUploadImage();
    }

    public boolean inputChatEditorAction(TextView tv, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            clickInputChat(tv.getContext());
            return true;
        }
        return false;
    }

    public void startUploadImage(final Activity activity, @NonNull final Uri data) {
        model.requestUploadPicture(data).addOnProgressListener(activity, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                view.showUploadProgress(taskSnapshot.getTotalByteCount(), taskSnapshot.getBytesTransferred());
            }
        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful() && task.getResult().getDownloadUrl() != null) {
                    // picture url
                    model.getChat().setPictureUrl(task.getResult().getDownloadUrl().toString());
                    return model.requestUploadChat(activity, model.getChat());
                }

                throw task.getException();
            }
        }).addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                view.successUploadImage(data);
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.failureUploadImage(e);
            }
        });
    }

    @BindingAdapter("setRecyclerView")
    public static void setRecyclerView(RecyclerView recyclerView, ChatViewModel viewModel) {
        if (recyclerView.getAdapter() == null) {
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
            manager.setStackFromEnd(true);
            recyclerView.setLayoutManager(manager);

            ChatRecyclerAdapter adapter = new ChatRecyclerAdapter(viewModel.getModel().getChatRef());
            recyclerView.setAdapter(adapter);
        }
    }
}
