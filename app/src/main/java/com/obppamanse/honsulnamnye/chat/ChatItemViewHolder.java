package com.obppamanse.honsulnamnye.chat;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.obppamanse.honsulnamnye.chat.model.Chat;
import com.obppamanse.honsulnamnye.databinding.ItemChatMeBinding;
import com.obppamanse.honsulnamnye.databinding.ItemChatOtherBinding;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatItemViewHolder extends RecyclerView.ViewHolder {

    private ChatItemViewModel viewModel;

    private ViewDataBinding binding;

    public ChatItemViewHolder(ViewDataBinding binding, ChatItemViewModel viewModel) {
        super(binding.getRoot());

        this.binding = binding;
        this.viewModel = viewModel;

        if (binding instanceof ItemChatMeBinding) {
            ((ItemChatMeBinding) binding).setViewModel(viewModel);
        } else if (binding instanceof ItemChatOtherBinding) {
            ((ItemChatOtherBinding) binding).setViewModel(viewModel);
        }
    }

    public void populateChat(Chat chat) {
        viewModel.setChat(chat);
        binding.executePendingBindings();
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}
