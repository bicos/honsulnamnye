package com.obppamanse.honsulnamnye.main;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.obppamanse.honsulnamnye.main.model.Category;

/**
 * Created by raehyeong.park on 2017. 11. 30..
 */

public class CategorySelectItemViewModel extends BaseObservable {

    private Category category;

    public CategorySelectItemViewModel() {
    }

    public void setCategory(Category category) {
        this.category = category;
        notifyChange();
    }

    @Bindable
    public String getCategoryName() {
        return category.getName();
    }

    public void clickCategory(Context context) {
        MainActivity.moveCategory(context, category);
    }
}
