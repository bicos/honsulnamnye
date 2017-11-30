package com.obppamanse.honsulnamnye.main;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.main.model.Category;

/**
 * Created by raehyeong.park on 2017. 11. 30..
 */

public class CategorySelectRecyclerAdapter extends FirebaseRecyclerAdapter<Category, CategorySelectViewHolder> {

    public CategorySelectRecyclerAdapter() {
        super(Category.class, R.layout.item_category, CategorySelectViewHolder.class, FirebaseUtils.getCategoryRef());
    }

    @Override
    protected void populateViewHolder(CategorySelectViewHolder viewHolder, Category model, int position) {
        viewHolder.populateViewHolder(model);
    }
}
