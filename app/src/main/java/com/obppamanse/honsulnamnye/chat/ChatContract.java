package com.obppamanse.honsulnamnye.chat;

import android.app.Activity;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.chat.model.Chat;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatContract {

    public interface View {

        void clearInputChat();

        void showErrorToast(Exception e);

        void moveScrollToPositionBottom();

        void showErrorToast(String msg);

        void chooseUploadImage();

        void showUploadProgress(long totalByteCount, long bytesTransferred);

        void successUploadImage(Uri downloadUrl);

        void failureUploadImage(Exception e);
    }

    public interface Model {

        Task<Void> requestUploadChat(Activity activity, Chat chat);

        Chat getChat();

        DatabaseReference getChatRef();

        UploadTask requestUploadPicture(Uri uri);
    }

    public static class MessageEmptyException extends Exception {

        private static final String MSG = "메세지를 입력하여 주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }
}
