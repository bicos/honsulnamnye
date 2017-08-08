package com.obppamanse.honsulnamnye.chat;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.obppamanse.honsulnamnye.chat.model.ChatItemViewModel;
import com.obppamanse.honsulnamnye.databinding.ItemChatMeBinding;
import com.obppamanse.honsulnamnye.databinding.ItemChatOtherBinding;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatItemViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding binding;

    public ChatItemViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public void populateChat(ChatItemViewModel chat) {
        if (binding instanceof ItemChatMeBinding) {
            ((ItemChatMeBinding) binding).setViewModel(chat);
        } else if (binding instanceof ItemChatOtherBinding) {
            ((ItemChatOtherBinding) binding).setViewModel(chat);
        }
        binding.executePendingBindings();
    }
}
