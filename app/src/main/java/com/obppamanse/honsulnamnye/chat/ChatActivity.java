package com.obppamanse.honsulnamnye.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.ActivityChatBinding;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatActivity extends AppCompatActivity implements ChatContract.View {

    private static final String PARAM_KEY = "key";

    private static final int REQUEST_UPLOAD_IMAGE = 1000;

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String key = getIntent().getStringExtra(PARAM_KEY);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        binding.setViewModel(new ChatViewModel(this, new ChatModel(key)));
    }

    @Override
    protected void onDestroy() {
        if (binding.chatList.getAdapter() != null) {
            ((ChatRecyclerAdapter) binding.chatList.getAdapter()).cleanup();
        }
        super.onDestroy();
    }

    @Override
    public void clearInputChat() {
        binding.inputChat.getText().clear();
    }

    @Override
    public void showErrorToast(Exception e) {
        Toast.makeText(this, "메세지 전송을 실패하였습니다. 원인[" + e.getMessage() + "]", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveScrollToPositionBottom() {
        binding.chatList.post(new Runnable() {
            @Override
            public void run() {
                binding.chatList.scrollToPosition(binding.chatList.getAdapter().getItemCount() - 1);
            }
        });
    }

    @Override
    public void showErrorToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void chooseUploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_select_image)), REQUEST_UPLOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UPLOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            binding.getViewModel().addUploadImageUri(data.getData());
        }
    }

    public static void start(Context context, String key) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(PARAM_KEY, key);
        context.startActivity(intent);
    }
}
