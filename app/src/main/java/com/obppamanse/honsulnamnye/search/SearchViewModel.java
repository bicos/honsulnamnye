package com.obppamanse.honsulnamnye.search;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.firebase.database.Query;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.timeline.TimeLineRecyclerAdapter;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class SearchViewModel extends BaseObservable {

    private SearchContract.Model model;

    private boolean isShowProgress;

    public SearchViewModel(SearchContract.View view, SearchContract.Model model) {
        this.model = model;
    }

    @Bindable
    public boolean getIsSearchKeywordEmpty(){
        return TextUtils.isEmpty(model.getSearchKeyword());
    }

    @Bindable
    public Query getSearchQuery(){
        return model.getSearchQuery();
    }

    @Bindable
    public boolean getIsShowProgress(){
        return isShowProgress;
    }

    @Bindable
    public SearchViewModel getViewModel(){
        return this;
    }

    public void setIsShowProgress(boolean isShowProgress) {
        this.isShowProgress = isShowProgress;
        notifyPropertyChanged(BR.isShowProgress);
    }

    public void clickSearchButton() {
        notifyPropertyChanged(BR.viewModel);
    }

    public void changeSearchKeyword(CharSequence sequence) {
        if (sequence != null) {
            model.setSearchKeyword(sequence.toString());
        }
        notifyPropertyChanged(BR.isSearchKeywordEmpty);
    }

    @BindingAdapter("setSearchList")
    public static void setSearchList(RecyclerView recyclerView, final SearchViewModel viewModel) {
        if (viewModel.getSearchQuery() == null) {
            return;
        }

        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }

        if (recyclerView.getAdapter() != null) {
            ((TimeLineRecyclerAdapter)recyclerView.getAdapter()).cleanup();
        }

        viewModel.setIsShowProgress(true);

        TimeLineRecyclerAdapter adapter = new TimeLineRecyclerAdapter(viewModel.getSearchQuery()) {
            @Override
            public void onDataChanged() {
                if (viewModel.getIsShowProgress()) {
                    viewModel.setIsShowProgress(false);
                }
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("showProgress")
    public static void showProgress(ContentLoadingProgressBar progressBar, boolean isShowProgress) {
        if (isShowProgress) {
            progressBar.show();
        } else {
            progressBar.hide();
        }
    }
}
