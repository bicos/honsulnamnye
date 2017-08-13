package com.obppamanse.honsulnamnye.user.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (info == null || firebaseUser == null) {
            Toast.makeText(getApplicationContext(), R.string.error_not_exist_user_info, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        binding.setViewModel(new UserProfileViewModel(this, new UserProfileModel(info, firebaseUser)));
    }

    @Override
    public void chooseProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_select_image)), REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void setUiSuccessWithdrawalService() {
        Toast.makeText(getApplicationContext(), R.string.msg_withdrawal_service, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void setUiFailedWithdrawalService(Exception exception) {
        Toast.makeText(getApplicationContext(), R.string.error_withdrawal_service, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUiSuccessModifyProfile() {
        Toast.makeText(getApplicationContext(), R.string.msg_success_modify_user_info, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void setUiFailedModifyProfile(Exception e) {
        Toast.makeText(getApplicationContext(), R.string.error_failed_modify_user_info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUploadProfileImageProgress() {
        binding.containerLoadingProgress.setVisibility(View.VISIBLE);
        binding.msgProgress.setText(R.string.msg_progress_upload_image);
    }

    @Override
    public void showUpdateFirebaseUserProfileProgress() {
        binding.containerLoadingProgress.setVisibility(View.VISIBLE);
        binding.msgProgress.setText(R.string.msg_progress_update_user_info);
    }


    @Override
    public void showUpdateUserInfoProgress() {
        binding.containerLoadingProgress.setVisibility(View.VISIBLE);
        binding.msgProgress.setText(R.string.msg_progress_update_user_info);
    }

    @Override
    public void hideAllProgress() {
        binding.containerLoadingProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            binding.getViewModel().changeProfileImage(data.getData());
        }
    }

    @Override
    public void onBackPressed() {
        final UserProfileViewModel viewModel = binding.getViewModel();
        if (viewModel.isModifyUserInfo()) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_alert)
                    .setMessage(R.string.msg_exist_modify_user_info)
                    .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.clickModifyProfile(UserProfileActivity.this);
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    viewModel.initChangeFlag();
                }
            });
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    public static void startUserProfileActivity(Context context, UserInfo info){
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra("user", info);
        context.startActivity(intent);
    }
}
