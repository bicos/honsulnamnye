package com.obppamanse.honsulnamnye.post;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.main.model.Category;

/**
 * Created by raehyeong.park on 2017. 12. 4..
 */

public class CategorySelectAdapter extends FirebaseListAdapter<Category> implements AdapterView.OnItemSelectedListener {

    private final PostContract.SetModel setter;

    public CategorySelectAdapter(Context context, PostContract.SetModel setter) {
        super(context, Category.class, android.R.layout.simple_spinner_dropdown_item, FirebaseUtils.getCategoryRef());
        this.setter = setter;
    }

    @Override
    protected void populateView(View v, Category model, int position) {
        ((CheckedTextView) v).setText(model.getName());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setter.setCategory(getItem(i).getCode());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
