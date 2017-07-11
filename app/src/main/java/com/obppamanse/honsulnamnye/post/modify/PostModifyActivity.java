package com.obppamanse.honsulnamnye.post.modify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.ActivityPostModifyBinding;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;
import com.obppamanse.honsulnamnye.post.write.MapsActivity;

import static com.obppamanse.honsulnamnye.post.write.MapsActivity.PARAM_SELECT_PLACE;

/**
 * Created by raehyeong.park on 2017. 7. 11..
 */

public class PostModifyActivity extends AppCompatActivity implements PostContract.ModifyView{

    public static final String PARAM_POST = "post";

    private static final int REQUEST_SELECT_LOCATION = 1000;

    ActivityPostModifyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Post post;

        Intent intent = getIntent();
        if (intent != null) {
            post = intent.getParcelableExtra(PARAM_POST);
        } else {
            showToastError();
            return;
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_modify);
        PostContract.ModifyModel model = new PostModifyModel(post);
        binding.setViewModel(new PostModifyViewModel(model, this));
    }

    private void showToastError() {
        Toast.makeText(this, R.string.failure_modify_post, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showProgress() {
        binding.loadingProgress.show();
    }

    @Override
    public void dismissProgress() {
        binding.loadingProgress.hide();
    }

    @Override
    public void successModifyPost() {
        Toast.makeText(this, R.string.success_modify_post, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failureModifyPost() {
        Toast.makeText(this, R.string.failure_modify_post, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorWrongDueDate() {
        Toast.makeText(getContext(), R.string.error_wrong_due_date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startSelectLocation() {
        startActivityForResult(new Intent(getContext(), MapsActivity.class), REQUEST_SELECT_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_LOCATION && resultCode == RESULT_OK) {
            binding.getViewModel().updatePlace((Place) data.getParcelableExtra(PARAM_SELECT_PLACE));
        }
    }

    public static void start(Activity activity, Post post) {
        Intent intent = new Intent(activity, PostModifyActivity.class);
        intent.putExtra(PARAM_POST, post);
        activity.startActivity(intent);
    }
}
