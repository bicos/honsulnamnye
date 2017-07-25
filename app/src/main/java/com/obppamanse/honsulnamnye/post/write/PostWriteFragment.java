package com.obppamanse.honsulnamnye.post.write;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.FragmentPostWriteBinding;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;

import static com.obppamanse.honsulnamnye.post.write.MapsActivity.PARAM_SELECT_PLACE;

/**
 * Created by raehyeong.park on 2017. 5. 26..
 */

public class PostWriteFragment extends Fragment implements PostContract.WriteView {

    private static final int REQUEST_SELECT_LOCATION = 1000;

    private static final int REQUEST_UPLOAD_IMAGE = 1001;

    public PostWriteFragment() {
    }

    public static PostWriteFragment newInstance() {

        Bundle args = new Bundle();

        PostWriteFragment fragment = new PostWriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentPostWriteBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostWriteBinding.inflate(inflater, container, false);
        binding.setViewModel(new PostWriteViewModel(this, new PostWriteModel()));
        binding.loadingProgress.hide();
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
    public void showErrorWrongDueDate() {
        Toast.makeText(getContext(), "잘못된 약속 날짜입니다. 오늘 날짜 이상을 선택해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startSelectLocation() {
        startActivityForResult(new Intent(getContext(), MapsActivity.class), REQUEST_SELECT_LOCATION);
    }

    @Override
    public void chooseProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_select_image)), REQUEST_UPLOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_LOCATION && resultCode == Activity.RESULT_OK && data != null) {
            if (data.hasExtra(PARAM_SELECT_PLACE)) {
                binding.getViewModel().updatePlace((Place) data.getParcelableExtra(PARAM_SELECT_PLACE));
            }
        } else if (requestCode == REQUEST_UPLOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            binding.getViewModel().addUploadImageUri(data.getData());
        }
    }

    @Override
    public void showProgress() {
        binding.loadingProgress.show();
    }

    @Override
    public void dismissProgress() {
        binding.loadingProgress.hide();
    }
}
