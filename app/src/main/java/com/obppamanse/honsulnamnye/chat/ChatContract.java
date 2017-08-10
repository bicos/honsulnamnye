package com.obppamanse.honsulnamnye.chat;

import android.app.Activity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.obppamanse.honsulnamnye.chat.model.Chat;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class ChatContract {

    public interface View {

        void clearInputChat();

        void showErrorToast(Exception e);

        void moveScrollToPositionBottom();
    }

    public interface Model {

        void requestInputChat(Activity activity, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

        Chat getChat();

        DatabaseReference getChatRef();
    }

    public class MessageEmptyException extends Exception {

        private static final String MSG = "메세지를 입력하여 주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }
}
