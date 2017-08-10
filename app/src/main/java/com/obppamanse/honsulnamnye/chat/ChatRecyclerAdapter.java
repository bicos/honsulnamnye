package com.obppamanse.honsulnamnye.chat;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.chat.model.Chat;
import com.obppamanse.honsulnamnye.chat.model.ChatItemViewModel;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatRecyclerAdapter extends FirebaseRecyclerAdapter<Chat, ChatItemViewHolder> {

    private FirebaseUser user;

    public ChatRecyclerAdapter(Query ref) {
        super(Chat.class, R.layout.item_chat_me, ChatItemViewHolder.class, ref);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public ChatItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding;

        if (viewType == R.layout.item_chat_me || viewType == R.layout.item_chat_other) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        } else {
            throw new IllegalArgumentException("viewType argument not valid");
        }

        return new ChatItemViewHolder(binding, new ChatItemViewModel());
    }

    @Override
    protected void populateViewHolder(ChatItemViewHolder viewHolder, Chat model, int position) {
        viewHolder.populateChat(model);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getUserInfo().uid.equals(user.getUid())) {
            // 자신이 쓴 채팅이라면
            return R.layout.item_chat_me;
        } else {
            // 남이 쓴 채팅이라면
            return R.layout.item_chat_other;
        }
    }
}
