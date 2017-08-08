package com.obppamanse.honsulnamnye.post;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.obppamanse.honsulnamnye.post.detail.PostDetailModel;
import com.obppamanse.honsulnamnye.post.model.Place;

import java.util.List;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class PostContract {

    public interface SetModel {

        void setTitle(String title);

        void setDesc(String desc);

        void setPlace(Place place);

        void setDueDate(long timeMillisecond);
    }

    public interface GetModel {

        String getTitle();

        String getDesc();

        Place getPlace();

        long getDueDate();

        long getWriteDate();

        boolean isWriter();

        String getPostKey();
    }

    public interface WriteModel extends SetModel {

        void writePost(Activity activity, OnSuccessListener<Void> successListener, OnFailureListener failureListener) throws Exception;

        String getPlaceName();

        long getDueDate();

        void addUploadImageUri(Uri data);

        List<Uri> getUploadImageUri();
    }

    public interface ModifyModel extends GetModel, SetModel {
        void modifyPost(Activity activity, OnCompleteListener<Void> listener) throws Exception;
    }

    public interface DetailModel extends GetModel {
        void deletePost(Activity activity, OnCompleteListener<Void> listener) throws Exception;

        void joinGroup(Activity activity, OnCompleteListener<Void> listener) throws Exception;

        void withdrawalGroup(Activity activity, OnCompleteListener<Void> listener) throws Exception;

        void requestIsMember(Activity activity, final PostDetailModel.MemberExistListener listener);

        List<StorageReference> getImageUrlList();
    }

    public interface View {
        Context getContext();

        void showProgress();

        void dismissProgress();
    }

    public interface WriteView extends View {
        void successWritePost();

        void failureWritePost(Exception e);

        void showErrorWrongDueDate();

        void startSelectLocation();

        void chooseUploadImage();
    }

    public interface ModifyView extends View {
        void successModifyPost();

        void failureModifyPost();

        void showErrorWrongDueDate();

        void startSelectLocation();
    }

    public interface DetailView extends View {

        void successDeletePost();

        void failureDeletePost(Exception e);

        void successJoinGroup();

        void failureJoinGroup(Exception e);

        void successWithdrawalGroup();

        void failureWithdrawalGroup(Exception e);

        void successCreateChatRoom(String newChatKey);

        void failureCreateChatRoom(Exception e);
    }

    public static class NotExistAuthUserException extends Exception {

        private static final String MSG = "로그인을 한 후 글쓰기를 해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }

    public static class EmptyDescPostException extends Exception {
        private static final String MSG = "모임 설명이 비어있습니다. 모임 설명을 입력하여 주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }

    public static class EmptyTitlePostException extends Exception {
        private static final String MSG = "모임 제목이 비어있습니다. 모임 제목을 입력하여 주세요.";

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

    public static class FailureJoinGroupException extends Exception {
        private static final String MSG = "그룹 가입을 실패하였습니다. 다시 시도해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }

    public static class FailureModifyPostException extends Exception {
        private static final String MSG = "글 수정을 실패하였습니다. 다시 시도해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }
}
