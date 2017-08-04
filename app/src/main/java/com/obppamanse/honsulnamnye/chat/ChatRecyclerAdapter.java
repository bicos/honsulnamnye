package com.obppamanse.honsulnamnye.chat;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.chat.model.ChatItemViewModel;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatRecyclerAdapter extends FirebaseRecyclerAdapter<ChatItemViewModel, ChatItemViewHolder> {

    public ChatRecyclerAdapter(Query ref) {
        super(ChatItemViewModel.class, R.layout.item_chat, ChatItemViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(ChatItemViewHolder viewHolder, ChatItemViewModel model, int position) {
        viewHolder.populateChat(model);
    }
}
