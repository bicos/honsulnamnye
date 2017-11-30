package com.obppamanse.honsulnamnye.main;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.obppamanse.honsulnamnye.databinding.ItemCategoryBinding;
import com.obppamanse.honsulnamnye.main.model.Category;

/**
 * Created by raehyeong.park on 2017. 11. 30..
 */

public class CategorySelectViewHolder extends RecyclerView.ViewHolder {

    private CategorySelectItemViewModel viewModel;

    private ItemCategoryBinding binding;

    public CategorySelectViewHolder(View itemView) {
        super(itemView);

        binding = DataBindingUtil.bind(itemView);
        viewModel = new CategorySelectItemViewModel();
        binding.setViewModel(viewModel);
    }

    public void populateViewHolder(Category category) {
        if (viewModel != null) {
            viewModel.setCategory(category);
        }
        if (binding != null) {
            binding.executePendingBindings();
        }
    }
}
