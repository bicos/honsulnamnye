package com.obppamanse.honsulnamnye.chat.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

@IgnoreExtraProperties
public class Chat {

    private UserInfo userInfo;

    private long timestamp;

    private String msg;

    private String key;

    public Chat() {
    }

    public Chat(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Chat(Chat chat) {
        userInfo = chat.getUserInfo();
        timestamp = chat.getTimestamp();
        msg = chat.getMsg();
        key = chat.getKey();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
