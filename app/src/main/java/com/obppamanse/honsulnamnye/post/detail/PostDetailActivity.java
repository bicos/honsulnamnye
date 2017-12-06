package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.obppamanse.honsulnamnye.chat.ChatActivity;
import com.obppamanse.honsulnamnye.databinding.ActivityPostDetailBinding;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Post;
import com.obppamanse.honsulnamnye.post.modify.PostModifyActivity;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostDetailActivity extends AppCompatActivity implements PostContract.DetailView, OnMapReadyCallback {

    public static final String PARAM_POST_KEY = "post_key";

    private PostDetailViewModel viewModel;

    private ActivityPostDetailBinding binding;

    private Post post;

    private String postKey;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dismissProgress();
            post = dataSnapshot.getValue(Post.class);
            populatePostDetail(post);
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
        postKey = intent.getStringExtra(PARAM_POST_KEY);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);
        setSupportActionBar(binding.toolbar);

        showProgress();

        FirebaseUtils.getPostRef().child(postKey).addValueEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        FirebaseUtils.getPostRef().child(postKey).removeEventListener(listener);
        super.onDestroy();
    }

    private void showToastError() {
        Toast.makeText(getActivity(), R.string.msg_error_showing_post, Toast.LENGTH_SHORT).show();
    }

    private void populatePostDetail(Post post) {
        if (post == null) {
            return;
        }

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
                        .setTitle(R.string.title_alert)
                        .setMessage(R.string.msg_delete_post)
                        .setPositiveButton(R.string.button_ok, (dialogInterface, i) ->
                                viewModel.clickDeletePost(PostDetailActivity.this))
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
                return true;
            case R.id.action_modify:
                PostModifyActivity.start(this, post);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Activity getActivity() {
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
        Toast.makeText(this, R.string.msg_success_delete_post, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failureDeletePost(Exception e) {
        Toast.makeText(this, R.string.msg_delete_post_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successJoinGroup() {
        Toast.makeText(this, R.string.msg_success_join_group, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failureJoinGroup(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successWithdrawalGroup() {
        Toast.makeText(this, R.string.msg_success_exit_group, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failureWithdrawalGroup(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successCreateChatRoom(String newChatKey) {
        Toast.makeText(this, R.string.msg_success_create_chat_room, Toast.LENGTH_SHORT).show();
        ChatActivity.start(this, newChatKey);
    }

    @Override
    public void failureCreateChatRoom(Exception e) {
        Toast.makeText(this, R.string.msg_failed_create_chat_room, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlertWithdrawalGroup(DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this).setTitle(R.string.title_alert)
                .setMessage(R.string.msg_exit_group)
                .setPositiveButton(R.string.button_ok, onClickListener)
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    @Override
    public void showAlertCreateChatRoom(DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this).setTitle(R.string.title_alert)
                .setMessage("채팅방이 존재하지 않습니다. 채팅방을 만드시겠습니까?")
                .setPositiveButton(R.string.button_ok, onClickListener)
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    @Override
    public void failureJoinChatRoom(Exception e) {
        Toast.makeText(this, "채팅방 가입을 실패하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public static void start(Context context, String postKey) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(PARAM_POST_KEY, postKey);
        context.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        viewModel.updateGoogleMap(googleMap);
    }
}
