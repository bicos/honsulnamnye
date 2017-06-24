package com.obppamanse.honsulnamnye.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.obppamanse.honsulnamnye.SplashActivity;
import com.obppamanse.honsulnamnye.databinding.FragmentSideMenuBinding;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class SideMenuFragment extends Fragment implements SideMenuContract.View {


    public SideMenuFragment() {
    }

    public static SideMenuFragment newInstance() {

        Bundle args = new Bundle();

        SideMenuFragment fragment = new SideMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSideMenuBinding binding = FragmentSideMenuBinding.inflate(inflater, container, false);
        binding.setViewModel(new SideMenuViewModel(this, new SideMenuRequest()));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void successLogout() {
        startActivity(new Intent(getContext(), SplashActivity.class));
        getActivity().finish();
    }

    @Override
    public void failedGetUserInfo(Exception e) {
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
