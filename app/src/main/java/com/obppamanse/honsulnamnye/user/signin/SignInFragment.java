package com.obppamanse.honsulnamnye.user.signin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.SignUpActivity;
import com.obppamanse.honsulnamnye.SplashActivity;
import com.obppamanse.honsulnamnye.databinding.FragmentSignInBinding;

/**
 * Created by raehyeong.park on 2017. 4. 25..
 */

public class SignInFragment extends Fragment implements SignInContract.View {

    private SignInViewModel model;

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SignInFragment() {

    }

    FragmentSignInBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        binding.loadingProgress.hide();

        model = new SignInViewModel(this, new SignInModel());
        binding.setViewModel(model);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (model != null) {
            model.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void startMainActivity() {
        SplashActivity.startMainActivity(getActivity());
    }

    @Override
    public void showUserNotFoundAlertDialog(DialogInterface.OnClickListener positiveClickListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.msg_guide_move_sign_up)
                .setPositiveButton(R.string.txt_ok, positiveClickListener)
                .setNegativeButton(R.string.txt_cancel, null)
                .show();
    }

    @Override
    public void startSignUpActivity() {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showException(Exception e) {
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        binding.loadingProgress.show();
    }

    @Override
    public void hideProgress() {
        binding.loadingProgress.hide();
    }
}
