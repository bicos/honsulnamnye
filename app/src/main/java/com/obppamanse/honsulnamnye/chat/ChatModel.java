package com.obppamanse.honsulnamnye.chat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.obppamanse.honsulnamnye.chat.model.Chat;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatModel implements ChatContract.Model {

    private DatabaseReference chatRef;

    private FirebaseUser user;

    private Chat chat;

    public ChatModel(String key) {
        String chatKey;
        if (TextUtils.isEmpty(key)) {
            chatKey = FirebaseUtils.getChatRef().push().getKey();
        } else {
            chatKey = key;
        }

        chatRef = FirebaseUtils.getChatRef().child(chatKey);
        user = FirebaseAuth.getInstance().getCurrentUser();
        chat = new Chat(user.getUid(), user.getDisplayName());
    }

    @Override
    public void requestInputChat(final Activity activity,
                                 final OnSuccessListener<Void> successListener,
                                 final OnFailureListener failureListener) {
        if (TextUtils.isEmpty(chat.getMsg())) {
            failureListener.onFailure(new PostContract.EmptyDescPostException());
            return;
        }
        final String chatKey = chatRef.push().getKey();
        chat.setKey(chatKey);
        chatRef.child(chatKey).setValue(chat)
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        if (task.isSuccessful()) {
                            return chatRef.child(chatKey).child("timestamp").setValue(ServerValue.TIMESTAMP)
                                    .addOnSuccessListener(activity, successListener)
                                    .addOnFailureListener(activity, failureListener);
                        } else {
                            failureListener.onFailure(task.getException());
                            return task;
                        }
                    }
                });
    }

    @Override
    public Chat getChat() {
        return chat;
    }

    @Override
    public DatabaseReference getChatRef() {
        return chatRef;
    }
}
