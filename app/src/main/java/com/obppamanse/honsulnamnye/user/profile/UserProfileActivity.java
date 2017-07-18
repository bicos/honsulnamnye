package com.obppamanse.honsulnamnye.user.profile;

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

    private static final int REQUEST_CODE_PICK_IMAGE = 1000;

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

    @Override
    public void chooseProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지 선택"), REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            binding.getViewModel().changeProfileImage(data.getData());
        }
    }

    public static void startUserProfileActivity(Context context, UserInfo info){
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra("user", info);
        context.startActivity(intent);
    }
}
