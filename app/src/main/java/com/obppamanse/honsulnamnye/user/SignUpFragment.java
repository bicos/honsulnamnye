package com.obppamanse.honsulnamnye.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.obppamanse.honsulnamnye.SplashActivity;
import com.obppamanse.honsulnamnye.databinding.FragmentSignUpBinding;

/**
 * Created by raehyeong.park on 2017. 4. 27..
 */

public class SignUpFragment extends Fragment implements SignUpContract.View {

    public static final int REQUEST_CODE_PICK_IMAGE = 1000;

    private static final String ARG_AUTH_INFO = "auth_info";

    private SignUpViewModel signUpViewModel;

    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSignUpBinding binding = FragmentSignUpBinding.inflate(inflater, container, false);

        signUpViewModel = new SignUpViewModel(this);
        binding.setViewModel(signUpViewModel);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showException(Exception e) {
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startMainActivity() {
        SplashActivity.startMainActivity(getActivity());
    }

    @Override
    public void chooseProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지 선택"), REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                signUpViewModel.setProfileImage(data.getData());
            } else {
                Toast.makeText(getContext(), "프로필 이미지 불러오기를 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
