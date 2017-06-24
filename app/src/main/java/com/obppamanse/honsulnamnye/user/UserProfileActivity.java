package com.obppamanse.honsulnamnye.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.ActivityUserProfileBinding;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 24..
 */

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.View {

    private ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserInfo info = null;

        if (getIntent() != null) {
            info = getIntent().getParcelableExtra("user");
        }

        if (info == null) {
            finish();
            return;
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        binding.setViewModel(new UserProfileViewModel(this, new UserProfileModel(info)));
    }

    public static void startUserProfileActivity(Context context, UserInfo info){
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra("user", info);
        context.startActivity(intent);
    }
}
