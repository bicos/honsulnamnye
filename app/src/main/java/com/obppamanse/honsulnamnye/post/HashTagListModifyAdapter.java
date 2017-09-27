package com.obppamanse.honsulnamnye.post;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.obppamanse.honsulnamnye.R;

/**
 * Created by raehyeong.park on 2017. 9. 25..
 */

public class HashTagListModifyAdapter extends FirebaseRecyclerAdapter<String, HashTagListModifyAdapter.HashTagViewHolder> {

    private boolean canModify;

    public HashTagListModifyAdapter(Query q, boolean canModify) {
        super(String.class, R.layout.item_hash_tag, HashTagViewHolder.class, q);
        this.canModify = canModify;
    }

    @Override
    protected void populateViewHolder(HashTagViewHolder viewHolder, String model, int position) {
        viewHolder.setData(model, getRef(position), canModify);
    }

    public static class HashTagViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHashTag;

        public HashTagViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(String tag, final DatabaseReference reference, boolean canModify) {
            tvHashTag = itemView.findViewById(R.id.btn_hash_tag);
            tvHashTag.setText(tag);

            if (canModify) {
                tvHashTag.setCompoundDrawablesWithIntrinsicBounds(null,
                        null,
                        ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_clear_white_24dp),
                        null);
                tvHashTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.removeValue();
                    }
                });
            } else {
                tvHashTag.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }
}
