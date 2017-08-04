package com.obppamanse.honsulnamnye.chat.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.user.model.UserInfo;
import com.obppamanse.honsulnamnye.util.DateUtils;

import static com.obppamanse.honsulnamnye.util.DateUtils.SIMPLE_DATE_FORMAT;

/**
 * Created by raehyeong.park on 2017. 8. 4..
 */

public class ChatItemViewModel extends BaseObservable {

    private Chat chat;

    private UserInfo userInfo;

    private FirebaseUser me;

    public ChatItemViewModel(Chat chat) {
        this.chat = chat;
        this.me = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUtils.getUserRef().child(chat.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                notifyPropertyChanged(BR.userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    @Bindable
    public Chat getChat() {
        return chat;
    }

    @Bindable
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Bindable
    public boolean getIsMyChat() {
        return me != null && me.getUid().equals(chat.getUid());
    }

    @Bindable
    public String getDateStr() {
        return DateUtils.getDateStr(Long.valueOf(chat.getTimeStamp()), SIMPLE_DATE_FORMAT);
    }
}
