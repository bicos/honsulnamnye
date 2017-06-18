package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Location;
import com.obppamanse.honsulnamnye.post.model.Participant;
import com.obppamanse.honsulnamnye.post.model.Post;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostDetailModel implements PostContract.DetailModel {

    private Post post;

    private DatabaseReference reference;

    public PostDetailModel(Post post) {
        this.reference = FirebaseUtils.getPostRef().child(post.getKey());
        this.post = post;
    }

    @Override
    public String getTitle() {
        return post != null ? post.getTitle() : null;
    }

    @Override
    public String getDesc() {
        return post != null ? post.getDesc() : null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public long getDueDate() {
        return post.getDueDateTime();
    }

    @Override
    public long getWriteDate() {
        return post.getWriteTime();
    }

    @Override
    public boolean isWriter() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && user.getUid().equals(post.getUid());
    }

    @Override
    public String getPostKey() {
        return post.getKey();
    }

    @Override
    public void deletePost(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        reference.removeValue().addOnCompleteListener(activity, listener);
    }

    @Override
    public void joinGroup(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            throw new PostContract.NotExistAuthUserException();
        }

        Participant participant = new Participant(user.getUid(),
                user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null,
                user.getDisplayName());

        reference.child(FirebaseUtils.PARTICIPANT_LIST_REF)
                .child(participant.getUid())
                .setValue(participant)
                .addOnCompleteListener(activity, listener);
    }

    @Override
    public void withdrawalGroup(Activity activity, OnCompleteListener<Void> listener) throws Exception {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            throw new PostContract.NotExistAuthUserException();
        }

        Participant participant = new Participant(user.getUid(),
                user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null,
                user.getDisplayName());

        reference.child(FirebaseUtils.PARTICIPANT_LIST_REF)
                .child(participant.getUid())
                .removeValue()
                .addOnCompleteListener(activity, listener);
    }

    public void requestIsMember(Activity activity, final MemberExistListener listener) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            listener.isMember(false);
            return;
        }

        DatabaseReference ref = FirebaseUtils.getParticipantListRef(post.getKey());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.isMember(dataSnapshot.hasChild(user.getUid()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.isMember(false);
            }
        });
    }

    public interface MemberExistListener {
        void isMember(boolean isMember);
    }
}
