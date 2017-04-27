package com.obppamanse.honsulnamnye.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSignInBinding binding = FragmentSignInBinding.inflate(inflater, container, false);

        model = new SignInViewModel(this, new SignInModel(this));
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
}
