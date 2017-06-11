package com.obppamanse.honsulnamnye.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.obppamanse.honsulnamnye.databinding.ItemPostBinding;
import com.obppamanse.honsulnamnye.post.model.Post;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostItemViewHolder extends RecyclerView.ViewHolder {

    private PostItemViewModel viewModel;

    public PostItemViewHolder(View itemView) {
        super(itemView);

        ItemPostBinding binding = ItemPostBinding.bind(itemView);
        viewModel = new PostItemViewModel();
        binding.setViewModel(viewModel);
    }

    void populateViewHolder(Post post) {
        viewModel.setPost(post);
    }
}
