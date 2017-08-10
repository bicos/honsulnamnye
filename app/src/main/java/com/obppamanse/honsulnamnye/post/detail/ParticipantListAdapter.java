package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.chat.model.ChatRoom;
import com.obppamanse.honsulnamnye.databinding.ItemParticipantBinding;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Participant;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravy on 2017. 6. 18..
 */

public class ParticipantListAdapter extends FirebaseRecyclerAdapter<Participant, ParticipantListAdapter.ParticipantViewHolder> {

    private PostContract.DetailView view;

    public ParticipantListAdapter(PostContract.DetailView view, Query ref) {
        super(Participant.class, R.layout.item_participant, ParticipantViewHolder.class, ref);
        this.view = view;
    }

    @Override
    public ParticipantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ParticipantViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        viewHolder.setView(view);
        return viewHolder;
    }

    @Override
    protected void populateViewHolder(ParticipantViewHolder viewHolder, Participant participant, int position) {
        viewHolder.setUserId(participant.getUid());
    }

    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {

        private ItemParticipantBinding binding;

        private PostContract.DetailView view;

        public ParticipantViewHolder(View view) {
            super(view);
            this.binding = ItemParticipantBinding.bind(view);
        }

        public void setUserId(String userId) {
            FirebaseUtils.getUserRef().child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    if (userInfo != null) {
                        binding.setViewModel(new ParticipantViewModel(view, userInfo));
                        binding.executePendingBindings();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // do nothing
                }
            });
        }

        public void setView(PostContract.DetailView view) {
            this.view = view;
        }
    }

    public static class ParticipantViewModel extends BaseObservable {

        private FirebaseUser me;

        private UserInfo other;

        private PostContract.DetailView view;

        public ParticipantViewModel(PostContract.DetailView view, UserInfo other) {
            this.me = FirebaseAuth.getInstance().getCurrentUser();
            this.other = other;
            this.view = view;
        }

        @Bindable
        public UserInfo getUserInfo() {
            return other;
        }

        public void clickProfileImage(Context context) {
            if (me != null && !me.getUid().equals(other.uid)) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.title_alert)
                        .setMessage("해당 사용자와 채팅을 시작하시겠습니까?")
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startChat();
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
            }
        }

        private void startChat() {
            UserInfo updateMyInfo = new UserInfo(me);

            final String newChatKey = createChatRoomKey(updateMyInfo.uid, other.uid);

            ChatRoom chatRoom = new ChatRoom(newChatKey);

            chatRoom.userList.put(updateMyInfo.uid, updateMyInfo.uid);
            chatRoom.userList.put(other.uid, other.uid);

            Map<String, Object> updateMap = new HashMap<>();

            updateMap.put("/user/" + updateMyInfo.uid + "/chatList/" + newChatKey, chatRoom.toMap());
            updateMap.put("/user/" + other.uid + "/chatList/" + newChatKey, chatRoom.toMap());

            FirebaseDatabase.getInstance()
                    .getReference()
                    .updateChildren(updateMap)
                    .addOnSuccessListener((Activity) view.getContext(), new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            view.successCreateChatRoom(newChatKey);
                        }
                    })
                    .addOnFailureListener((Activity) view.getContext(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            view.failureCreateChatRoom(e);
                        }
                    });
        }

        private String createChatRoomKey(String myUid, String otherUid) {
            String[] args = new String[]{myUid, otherUid};
            Arrays.sort(args);
            return TextUtils.join("_", args);
        }
    }
}
