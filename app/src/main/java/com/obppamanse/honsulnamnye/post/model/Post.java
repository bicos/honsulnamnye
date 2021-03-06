package com.obppamanse.honsulnamnye.post.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
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
    private long timestamp;

    // 만날 시간
    private long dueDateTime;

    // 만나는 장소
    private Place place;

    // 업로드된 파일 이름들
    private List<String> fileNames = new ArrayList<>();

    // 가입자
    private Map<String, Participant> participantList = new HashMap<>();

    // 단체채팅방
    private String chatKey;

    private List<String> hashTags = new ArrayList<>();

    private String category;

    public Post() {
    }

    protected Post(Parcel in) {
        key = in.readString();
        uid = in.readString();
        title = in.readString();
        desc = in.readString();
        timestamp = in.readLong();
        dueDateTime = in.readLong();
        place = in.readParcelable(Place.class.getClassLoader());
        fileNames = in.createStringArrayList();
        chatKey = in.readString();
        int size = in.readInt();
        for(int i = 0; i < size; i++){
            String key = in.readString();
            Participant value = in.readParcelable(Participant.class.getClassLoader());
            participantList.put(key,value);
        }
        hashTags = in.createStringArrayList();
        category = in.readString();
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(uid);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeLong(timestamp);
        dest.writeLong(dueDateTime);
        dest.writeParcelable(place, flags);
        dest.writeStringList(fileNames);
        dest.writeString(chatKey);
        dest.writeInt(participantList.size());
        for(Map.Entry<String,Participant> entry : participantList.entrySet()){
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeStringList(hashTags);
        dest.writeString(category);
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Post{" +
                "key='" + key + '\'' +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", timestamp=" + timestamp +
                ", dueDateTime=" + dueDateTime +
                ", place=" + place +
                ", fileNames=" + fileNames +
                ", participantList=" + participantList +
                ", chatKey='" + chatKey + '\'' +
                ", hashTags=" + hashTags +
                ", category='" + category + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("uid", uid);
        map.put("title", title);
        map.put("desc", desc);
        map.put("timestamp", timestamp);
        map.put("dueDateTime", dueDateTime);
        map.put("place", place);
        map.put("fileNames", fileNames);
        map.put("participantList", participantList);
        map.put("chatKey", chatKey);
        map.put("hashTags", hashTags);
        map.put("category", category);
        return map;
    }
}
