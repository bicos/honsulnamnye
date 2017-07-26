package com.obppamanse.honsulnamnye.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by raehyeong.park on 2017. 4. 30..
 */

public class FirebaseUtils {

    private static final String USER_REF = "user";

    private static final String POST_REF = "post";

    public static final String PARTICIPANT_LIST_REF = "participantList";

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

    public static StorageReference getStorageRef() {
        return FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_REF);
    }

    public static StorageReference getProfileStorageRef() {
        return FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_REF).child("profile");
    }

    /**
     *
     * @param postKey {@link com.obppamanse.honsulnamnye.post.model.Post#key}
     * @return
     */
    public static StorageReference getPostStorageRef(String postKey) {
        return FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_REF).child("post").child(postKey);
    }
}
