package com.obppamanse.honsulnamnye.post.detail;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.ActivityPostDetailBinding;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Post;
import com.obppamanse.honsulnamnye.post.modify.PostModifyActivity;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostDetailActivity extends AppCompatActivity implements PostContract.DetailView, OnMapReadyCallback {

    public static final String PARAM_POST = "post";

    public static final String PARAM_POST_KEY = "post_key";

    private PostDetailViewModel viewModel;

    private ActivityPostDetailBinding binding;

    private Post post;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dismissProgress();
            populatePostDetail(dataSnapshot.getValue(Post.class));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            dismissProgress();
            showToastError();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            post = intent.getParcelableExtra(PARAM_POST);
        } else {
            showToastError();
            return;
        }

        if (post == null) {
            post = new Post();
            String postKey = intent.getStringExtra(PARAM_POST_KEY);
            if (postKey == null) {
                showToastError();
                return;
            }
            post.setKey(postKey);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);
        setSupportActionBar(binding.toolbar);
        showProgress();
        FirebaseUtils.getPostRef().child(post.getKey()).addValueEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        FirebaseUtils.getPostRef().child(post.getKey()).removeEventListener(listener);
        super.onDestroy();
    }

    private void showToastError() {
        Toast.makeText(getContext(), "오류가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }

    private void populatePostDetail(Post post) {
        if (post == null) {
            return;
        }

        Log.i("test", "post : " + post);

        setTitle(post.getTitle());

        viewModel = new PostDetailViewModel(this, new PostDetailModel(post));
        binding.setViewModel(viewModel);

        if (viewModel.getPlace() != null) {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.place_desc);
            if (fragment != null && fragment instanceof MapFragment) {
                ((MapFragment) fragment).getMapAsync(this);
            }
        }

        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewModel != null && viewModel.getIsWriter()) {
            getMenuInflater().inflate(R.menu.menu_post_detail, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("해당 글을 삭제하겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                viewModel.clickDeletePost(PostDetailActivity.this);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
                return true;
            case R.id.action_modify:
                PostModifyActivity.start(this, post);
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void successDeletePost() {
        Toast.makeText(this, "글을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failureDeletePost(Exception e) {
        Toast.makeText(this, "글 삭제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successJoinGroup() {
        Toast.makeText(this, "그룹 가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failureJoinGroup(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successWithdrawalGroup() {
        Toast.makeText(this, "그룹 탈퇴에 성공하였습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failureWithdrawalGroup(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public static void startPostDetailActivity(Context context, Post post) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(PARAM_POST, post);
        context.startActivity(intent);
    }

    public static void startPostDetailActivity(Context context, String postKey) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(PARAM_POST_KEY, postKey);
        context.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        viewModel.updateGoogleMap(googleMap);
    }
}
