package com.obppamanse.honsulnamnye.chat.model;

/**
 * Created by raehyeong.park on 2017. 8. 3..
 */

public class Chat {

    private String uid;
    private String name;
    private String timestamp;
    private String msg;

    public Chat() {
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

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
