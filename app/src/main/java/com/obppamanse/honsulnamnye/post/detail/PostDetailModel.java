package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.chat.model.ChatRoom;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Participant;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostDetailModel implements PostContract.DetailModel {

    private Post post;

    private FirebaseUser user;

    private DatabaseReference reference;

    public PostDetailModel(Post post) {
        this.reference = FirebaseUtils.getPostRef().child(post.getKey());
        this.post = post;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public String getTitle() {
        return post != null ? post.getTitle() : null;
    }

    @Override
    public String getDesc() {
        return post != null ? post.getDesc() : null;
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
        return user != null && user.getUid().equals(post.getUid());
    }

    @Override
    public String getPostKey() {
        return post.getKey();
    }

    @Override
    public List<String> getFileNames() {
        return post != null ? post.getFileNames() : Collections.emptyList();
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
    public void deletePost(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        reference.removeValue().addOnCompleteListener(activity, listener);
    }

    @Override
    public void joinGroup(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        if (user == null) {
            throw new PostContract.NotExistAuthUserException();
        }

        Participant participant = new Participant(user.getUid());

        reference.child(FirebaseUtils.PARTICIPANT_LIST_REF)
                .child(participant.getUid())
                .setValue(participant)
                .addOnCompleteListener(activity, listener);
    }

    @Override
    public void withdrawalGroup(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        if (user == null) {
            throw new PostContract.NotExistAuthUserException();
        }

        Participant participant = new Participant(user.getUid());

        reference.child(FirebaseUtils.PARTICIPANT_LIST_REF)
                .child(participant.getUid())
                .removeValue()
                .addOnCompleteListener(activity, listener);
    }

    public void requestIsMember(Activity activity, final MemberExistListener listener) {
        if (user == null) {
            listener.isMember(false);
            return;
        }

        DatabaseReference ref = FirebaseUtils.getParticipantListRef(post.getKey());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.isMember(dataSnapshot.hasChild(user.getUid()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.isMember(false);
            }
        });
    }

    @Override
    public String getChatKey() {
        return post.getChatKey();
    }

    @Override
    public Task<String> createChatRoom() {
        UserInfo updateMyInfo = new UserInfo(user);

        final String newChatKey = FirebaseUtils.getChatRef().push().getKey();

        ChatRoom chatRoom = new ChatRoom(newChatKey, post.getTitle());
        chatRoom.userList.put(updateMyInfo.uid, updateMyInfo.uid);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("/user/" + updateMyInfo.uid + "/chatList/" + newChatKey, chatRoom.toMap());
        updateMap.put("/chatInfo/" + newChatKey, chatRoom.toMap());
        updateMap.put("/post/" + post.getKey() +"/chatKey", newChatKey);

        return FirebaseDatabase.getInstance().getReference().updateChildren(updateMap)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return newChatKey;
                    }
                    throw task.getException();
                });
    }

    @Override
    public void joinChatRoom(Activity activity, final OnSuccessListener<String> successListener, final OnFailureListener failureListener) {
        FirebaseUtils.getChatInfoRef().child(post.getChatKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                    if (chatRoom == null) {
                        failureListener.onFailure(new PostContract.FailureJoinChatRoom());
                        return;
                    }

                    UserInfo updateMyInfo = new UserInfo(user);
                    if (chatRoom.userList == null) {
                        chatRoom.userList = new HashMap<>();
                    }
                    chatRoom.userList.put(updateMyInfo.uid, updateMyInfo.uid);

                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("/user/" + updateMyInfo.uid + "/chatList/" + chatRoom.key, chatRoom.toMap());
                    updateMap.put("/chatInfo/" + chatRoom.key, chatRoom.toMap());

                    FirebaseDatabase.getInstance().getReference().updateChildren(updateMap)
                            .continueWith(task -> {
                                if (task.isSuccessful()) {
                                    return chatRoom.key;
                                }

                                throw task.getException();
                            })
                            .addOnSuccessListener(successListener)
                            .addOnFailureListener(failureListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                failureListener.onFailure(databaseError.toException());
            }
        });
    }

    public interface MemberExistListener {
        void isMember(boolean isMember);
    }
}
