package com.obppamanse.honsulnamnye.post;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.obppamanse.honsulnamnye.post.model.Location;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class PostContract {

    public interface Model {

        void setTitle(String title);

        void setDesc(String desc);

        void setLocation(Location location);

        void setDueDate(long timeMill);
    }

    public interface GetModel {
        String getTitle();

        String getDesc();

        Location getLocation();

        long getDueDate();

        long getWriteDate();
    }

    public interface WriteModel extends Model {
        void writePost(Activity activity, OnCompleteListener<Void> listener) throws Exception;
    }

    public interface ModifyModel extends Model {
        void modifyPost(Activity activity, OnCompleteListener<Void> listener) throws Exception;
    }

    public interface DetailModel extends GetModel{
        void deletePost(Activity activity, OnCompleteListener<Void> listener) throws Exception;
    }

    public interface View {
        Context getContext();
    }

    public interface WriteView extends View {
        void successWritePost();

        void failureWritePost(Exception e);
    }

    public interface ModifyView extends View {
        void successModifyPost();

        void failureModifyPost();
    }

    public interface DeleteView extends View {

        void successDeletePost();

        void failureDeletePost(Exception e);
    }

    public static class NotExistAuthUserException extends Exception {

        private static final String MSG = "로그인을 한 후 글쓰기를 해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }

    public static class FailureWritePostException extends Exception {
        private static final String MSG = "글쓰기를 실패하였습니다. 다시 시도해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }

    public static class FailureModifyPostException extends Exception {
        private static final String MSG = "글쓰기를 실패하였습니다. 다시 시도해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }
}
