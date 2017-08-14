package com.obppamanse.honsulnamnye.chat;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.obppamanse.honsulnamnye.chat.model.Chat;
import com.obppamanse.honsulnamnye.user.model.UserInfo;
import com.obppamanse.honsulnamnye.util.DateUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.obppamanse.honsulnamnye.util.DateUtils.SIMPLE_DATE_FORMAT;

/**
 * Created by raehyeong.park on 2017. 8. 4..
 */

public class ChatItemViewModel extends BaseObservable {

    private Context context;

    private Chat mChat;

    public ChatItemViewModel(Context context) {
        this.context = context;
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

    @Bindable
    public Transformation getProfileTransform() {
        return new CropCircleTransformation(context);
    }

    @BindingAdapter({"setImage", "setTransform"})
    public static void setImage(ImageView imageView, String url, Transformation transformation) {
        if (url != null) {
            imageView.setVisibility(View.VISIBLE);
            DrawableRequestBuilder builder = Glide.with(imageView.getContext())
                    .load(url);
            if (transformation != null) {
                builder.bitmapTransform(transformation);
            }
            builder.into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
