package com.obppamanse.honsulnamnye.chat.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

@IgnoreExtraProperties
public class Chat {

    private String uid;
    private String name;
    private long timestamp;
    private String msg;
    private String key;

    public Chat() {
    }

    public Chat(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
