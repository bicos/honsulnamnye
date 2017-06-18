package com.obppamanse.honsulnamnye.post.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.ItemParticipantBinding;
import com.obppamanse.honsulnamnye.post.model.Participant;

/**
 * Created by Ravy on 2017. 6. 18..
 */

public class ParticipantListAdapter extends FirebaseRecyclerAdapter<Participant, ParticipantListAdapter.ParticipantViewHolder> {

    public ParticipantListAdapter(Query ref) {
        super(Participant.class, R.layout.item_participant, ParticipantViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(ParticipantViewHolder viewHolder, Participant model, int position) {
        viewHolder.setParticipant(model);
    }

    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {

        ItemParticipantBinding binding;

        public ParticipantViewHolder(View view) {
            super(view);
            this.binding = ItemParticipantBinding.bind(view);
        }

        public void setParticipant(Participant participant) {
            binding.setParticipant(participant);
            binding.executePendingBindings();
        }
    }
}
