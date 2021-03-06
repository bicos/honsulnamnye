package com.obppamanse.honsulnamnye.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    private SideMenuViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSideMenuBinding binding = FragmentSideMenuBinding.inflate(inflater, container, false);

        viewModel = new SideMenuViewModel(this, new SideMenuRequest());
        binding.setViewModel(viewModel);
        viewModel.startSyncUserInfo();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        if (viewModel != null) {
            viewModel.stopSyncUserInfo();
        }
        super.onDestroyView();
    }

    @Override
    public void successLogout() {

    }

    @Override
    public void failedGetUserInfo(Exception e) {
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
