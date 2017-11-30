package com.obppamanse.honsulnamnye.main;

/**
 * Created by raehyeong.park on 2017. 11. 30..
 */

public class CategorySelectViewModel {

    private CategorySelectContract.View view;

    private CategorySelectContract.Model model;

    public CategorySelectViewModel(CategorySelectContract.View view, CategorySelectContract.Model model) {
        this.view = view;
        this.model = model;
    }

}
