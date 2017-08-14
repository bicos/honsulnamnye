package com.obppamanse.honsulnamnye.chat;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.obppamanse.honsulnamnye.chat.model.Chat;
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

    @Bindable
    public boolean isPictureContent() {
        return mChat != null && !TextUtils.isEmpty(mChat.getPictureUrl());
    }

    @BindingAdapter("setProfileImage")
    public static void setProfileImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    @BindingAdapter("setPicture")
    public static void setPicture(final ImageView imageView, String url) {
        if (url != null) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(imageView.getContext())
                    .load(url)
                    .fitCenter()
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
