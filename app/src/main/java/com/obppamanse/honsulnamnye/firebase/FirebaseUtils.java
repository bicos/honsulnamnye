package com.obppamanse.honsulnamnye.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by raehyeong.park on 2017. 4. 30..
 */

public class FirebaseUtils {

    private static final String CHAT_REF = "chat";

    private static final String CHAT_INFO_REF = "chatInfo";

    private static final String USER_REF = "user";

    private static final String POST_REF = "post";

    public static final String POST_FILENAMES_REF = "fileNames";

    public static final String POST_HASH_TAG_REF = "hashTags";

    public static final String PARTICIPANT_LIST_REF = "participantList";

    public static final String PUSH_TOKEN_REF = "pushToken";

    public static final String TIMESTAMP_REF = "timestamp";

    public static DatabaseReference getUserRef() {
        return FirebaseDatabase.getInstance().getReference().child(USER_REF);
    }

    public static DatabaseReference getPostRef() {
        return FirebaseDatabase.getInstance().getReference().child(POST_REF);
    }

    public static DatabaseReference getParticipantListRef(String postKey) {
        return getPostRef().child(postKey).child(PARTICIPANT_LIST_REF);
    }

    private static final String STORAGE_REF = "gs://honsulnamnyeo.appspot.com";

    public static StorageReference getProfileStorageRef() {
        return FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_REF).child("profile");
    }

    /**
     * @param postKey {@link com.obppamanse.honsulnamnye.post.model.Post#key}
     * @return
     */
    public static StorageReference getPostStorageRef(String postKey) {
        return FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_REF).child(POST_REF).child(postKey);
    }

    public static DatabaseReference getChatRef() {
        return FirebaseDatabase.getInstance().getReference().child(CHAT_REF);
    }

    public static DatabaseReference getChatInfoRef() {
        return FirebaseDatabase.getInstance().getReference().child(CHAT_INFO_REF);
    }

    public static StorageReference getChatStorageRef(String chatKey) {
        return FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_REF).child(CHAT_REF).child(chatKey);
    }
}
