package com.obppamanse.honsulnamnye.search;

import android.app.Activity;

import com.google.firebase.database.Query;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class SearchContract {

    public interface View {

    }

    public interface Model {
        void setSearchKeyword(String keyword);

        Query getSearchQuery();

        String getSearchKeyword();
    }
}
