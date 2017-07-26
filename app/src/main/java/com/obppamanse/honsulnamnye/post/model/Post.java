package com.obppamanse.honsulnamnye.post.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class Post implements Parcelable {

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

    // 만나는 장소
    private Place place;

    // 업로드된 파일 이름들
    private List<String> fileNames = new ArrayList<>();

    // 가입자
    private Map<String, Participant> participantList;

    public Post() {
    }

    protected Post(Parcel in) {
        key = in.readString();
        uid = in.readString();
        title = in.readString();
        desc = in.readString();
        writeTime = in.readLong();
        dueDateTime = in.readLong();
        place = in.readParcelable(Place.class.getClassLoader());
        fileNames = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeLong(writeTime);
        dest.writeLong(dueDateTime);
        dest.writeParcelable(place, flags);
        dest.writeStringList(fileNames);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Map<String, Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(Map<String, Participant> participantList) {
        this.participantList = participantList;
    }

    public void addUploadFileName(String fileName) {
        fileNames.add(fileName);
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    @Override
    public String toString() {
        return "Post{" +
                "key='" + key + '\'' +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", writeTime=" + writeTime +
                ", dueDateTime=" + dueDateTime +
                ", place=" + place +
                ", fileNames=" + fileNames +
                ", participantList=" + participantList +
                '}';
    }
}
