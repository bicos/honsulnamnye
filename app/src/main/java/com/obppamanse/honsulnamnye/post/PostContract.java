package com.obppamanse.honsulnamnye.post;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.obppamanse.honsulnamnye.post.detail.PostDetailModel;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;

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

        void setHashTag(String hashTag);
    }

    public interface GetModel {

        String getTitle();

        String getDesc();

        Place getPlace();

        long getDueDate();

        long getTimestamp();

        boolean isWriter();

        String getPostKey();

        List<String> getFileNames();
    }

    public interface WriteModel extends SetModel {

        void writePost(Activity activity, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

        void addUploadImageUri(Uri data);

        List<Uri> getUploadImageUri();

        Post getPost();

        List<String> getHashTagList();

        String getHashTag();
    }

    public interface ModifyModel extends GetModel, SetModel {

        Task<Void> modifyPost();

        Task<Void> modifyUploadImage();

        void removeFileName(String fileName);
    }

    public interface DetailModel extends GetModel {
        void deletePost(Activity activity, OnCompleteListener<Void> listener) throws Exception;

        void joinGroup(Activity activity, OnCompleteListener<Void> listener) throws Exception;

        void withdrawalGroup(Activity activity, OnCompleteListener<Void> listener) throws Exception;

        void requestIsMember(Activity activity, final PostDetailModel.MemberExistListener listener);

        String getChatKey();

        Task<String> createChatRoom();

        void joinChatRoom(Activity activity, OnSuccessListener<String> successListener, OnFailureListener failureListener);
    }

    public interface View {
        Activity getActivity();

        void showProgress();

        void dismissProgress();
    }

    public interface WriteView extends View {
        void successWritePost();

        void failureWritePost(Exception e);

        void showErrorWrongDueDate();

        void startSelectLocation();

        void chooseUploadImage();

        void showErrorHashTagEmpty();
    }

    public interface ModifyView extends View {
        void successModifyPost();

        void failureModifyPost();

        void showErrorWrongDueDate();

        void startSelectLocation();

        void chooseUploadImage();

        void failureUploadImage();

        void successUploadImage(Uri data);

        void showUploadProgress(long totalByteCount, long bytesTransferred);

        void failureDeleteImage();

        void successDeleteImage();
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

        void showAlertWithdrawalGroup(DialogInterface.OnClickListener onClickListener);

        void showAlertCreateChatRoom(DialogInterface.OnClickListener onClickListener);

        void failureJoinChatRoom(Exception e);
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

    public static class FailureJoinChatRoom extends Exception {
        private static final String MSG = "채팅방 입장을 실패하였습니다. 다시 시도해주세요.";

        @Override
        public String getMessage() {
            return MSG;
        }
    }
}
