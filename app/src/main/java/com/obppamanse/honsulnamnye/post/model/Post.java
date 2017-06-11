package com.obppamanse.honsulnamnye.post.model;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class Post {

    // 글 id
    private String key;

    // 글쓴이
    private String uid;

    // 타이틀
    private String title;

    // 내용
    private String desc;

    // 글 쓴 시간
    private long writeTime;

    // 만날 시간
    private long dueDateTime;

    public Post() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(long writeTime) {
        this.writeTime = writeTime;
    }

    public long getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(long dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}
