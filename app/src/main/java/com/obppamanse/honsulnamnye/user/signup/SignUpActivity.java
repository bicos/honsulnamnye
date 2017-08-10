package com.obppamanse.honsulnamnye.user.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.ActivitySignUpBinding;
import com.obppamanse.honsulnamnye.main.MainActivity;

/**
 * Created by raehyeong.park on 2017. 4. 27..
 */

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    public static final int REQUEST_CODE_PICK_IMAGE = 1000;

    private static final String ARG_AUTH_INFO = "auth_info";

    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySignUpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = new SignUpViewModel(this, new SignUpModel());
        binding.setViewModel(signUpViewModel);
    }

    @Override
    public void showException(Exception e) {
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void chooseProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지 선택"), REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                signUpViewModel.setProfileImage(data.getData());
            } else {
                Toast.makeText(getApplicationContext(), "프로필 이미지 불러오기를 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }
}
