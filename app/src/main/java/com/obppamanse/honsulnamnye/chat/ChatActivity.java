package com.obppamanse.honsulnamnye.chat;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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

    private static final int IMAGE_UPLOAD_PROGRESS_ID = 1;

    private static final String CHANEL_IMAGE_UPLOAD_PROGRESS = "image_upload_progress";

    private ActivityChatBinding binding;

    private NotificationManager manager;

    private NotificationCompat.Builder builder;

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
    public void showUploadProgress(long totalByteCount, long bytesTransferred) {
        if (builder == null) {
            builder = new NotificationCompat.Builder(this, CHANEL_IMAGE_UPLOAD_PROGRESS);
            builder.setContentText("이미지를 업로드 중입니다.");
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setProgress((int) totalByteCount, (int) bytesTransferred, false);
        manager.notify(IMAGE_UPLOAD_PROGRESS_ID, builder.build());
    }

    @Override
    public void successUploadImage(Uri downloadUrl) {
        Intent resultIntent = new Intent(Intent.ACTION_VIEW);
        resultIntent.setData(downloadUrl);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        builder.setContentText("이미지 업로드를 완료하였습니다.");
        builder.setProgress(0, 0, false);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        manager.notify(IMAGE_UPLOAD_PROGRESS_ID, builder.build());
    }

    @Override
    public void failureUploadImage(Exception e) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UPLOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            if (manager == null) {
                manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }

            binding.getViewModel().startUploadImage(this, data.getData());
        }
    }

    public static void start(Context context, String key) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(PARAM_KEY, key);
        context.startActivity(intent);
    }
}
