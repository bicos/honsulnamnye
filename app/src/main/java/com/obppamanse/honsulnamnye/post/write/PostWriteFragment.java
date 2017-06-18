package com.obppamanse.honsulnamnye.post.write;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.obppamanse.honsulnamnye.databinding.FragmentPostWriteBinding;
import com.obppamanse.honsulnamnye.post.PostContract;

/**
 * Created by raehyeong.park on 2017. 5. 26..
 */

public class PostWriteFragment extends Fragment implements PostContract.WriteView {

    public PostWriteFragment() {
    }

    public static PostWriteFragment newInstance() {

        Bundle args = new Bundle();

        PostWriteFragment fragment = new PostWriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPostWriteBinding binding = FragmentPostWriteBinding.inflate(inflater, container, false);
        binding.setViewModel(new PostWriteViewModel(this, new PostWriteModel()));
        return binding.getRoot();
    }

    @Override
    public void successWritePost() {
        Toast.makeText(getContext(), "글쓰기를 완료하였습니다.", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void failureWritePost(Exception e) {
        Toast.makeText(getContext(), "글쓰기를 실패하였습니다. 원인 : ["+e.toString()+"]", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }
}
