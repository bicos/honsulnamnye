package com.obppamanse.honsulnamnye.search;

import android.text.TextUtils;

import com.google.firebase.database.Query;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class SearchModel implements SearchContract.Model {

    private String keyword;

    @Override
    public void setSearchKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Query getSearchQuery() {
        return TextUtils.isEmpty(keyword) ? null : FirebaseUtils.getPostRef().orderByChild("title").startAt(keyword).endAt(keyword + "\uf8ff");
    }

    @Override
    public String getSearchKeyword() {
        return keyword;
    }
}
