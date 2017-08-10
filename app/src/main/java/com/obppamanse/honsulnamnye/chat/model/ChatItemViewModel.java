package com.obppamanse.honsulnamnye.chat.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.obppamanse.honsulnamnye.user.model.UserInfo;
import com.obppamanse.honsulnamnye.util.DateUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.obppamanse.honsulnamnye.util.DateUtils.SIMPLE_DATE_FORMAT;

/**
 * Created by raehyeong.park on 2017. 8. 4..
 */

public class ChatItemViewModel extends BaseObservable {

    private Chat mChat;

    public ChatItemViewModel() {
    }

    public void setChat(Chat chat) {
        mChat = chat;
        notifyChange();
    }

    @Bindable
    public Chat getChat() {
        return mChat;
    }

    @Bindable
    public UserInfo getUserInfo() {
        return mChat.getUserInfo();
    }

    @Bindable
    public String getDateStr() {
        return mChat != null ? DateUtils.getDateStr(mChat.getTimestamp(), SIMPLE_DATE_FORMAT) : null;
    }

    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView imageView, UserInfo userInfo) {
        if (userInfo != null) {
            Glide.with(imageView.getContext())
                    .load(userInfo.profileUri)
                    .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                    .into(imageView);
        }
    }
}
