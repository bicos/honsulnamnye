package com.obppamanse.honsulnamnye.post.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.ItemParticipantBinding;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.model.Participant;
import com.obppamanse.honsulnamnye.user.model.UserInfo;

/**
 * Created by Ravy on 2017. 6. 18..
 */

public class ParticipantListAdapter extends FirebaseRecyclerAdapter<Participant, ParticipantListAdapter.ParticipantViewHolder> {

    public ParticipantListAdapter(Query ref) {
        super(Participant.class, R.layout.item_participant, ParticipantViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(ParticipantViewHolder viewHolder, Participant participant, int position) {
        viewHolder.setUserId(participant.getUid());
    }

    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {

        ItemParticipantBinding binding;

        public ParticipantViewHolder(View view) {
            super(view);
            this.binding = ItemParticipantBinding.bind(view);
        }

        public void setUserId(String userId) {
            FirebaseUtils.getUserRef().child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    if (userInfo != null) {
                        binding.setUserInfo(userInfo);
                        binding.executePendingBindings();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // do nothing
                }
            });
        }
    }
}
