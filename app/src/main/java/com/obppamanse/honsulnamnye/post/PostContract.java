package com.obppamanse.honsulnamnye.post;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class PostContract {

    public interface Model {

        void writePost(Activity activity, OnCompleteListener<Void> listener) throws Exception;

        void modifyPost(Activity activity, OnCompleteListener<Void> listener) throws Exception;

        void deletePost(Activity activity, OnCompleteListener<Void> listener) throws Exception;
    }

    public interface View {

        void successWritePost();

        void failureWritePost();

        void successModifyPost();

        void failureModifyPost();

        void successDeletePost();

        void failureDeletePost();
    }

    public static class NotExistAuthUserException extends Exception {

        private static final String MSG = "로그인을 한 후 글쓰기를 해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }

    public static class FailureWritePostException extends Exception {
        private  static final String MSG = "글쓰기를 실패하였습니다. 다시 시도해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }

    public static class FailureModifyPostException extends Exception {
        private  static final String MSG = "글쓰기를 실패하였습니다. 다시 시도해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }
}
