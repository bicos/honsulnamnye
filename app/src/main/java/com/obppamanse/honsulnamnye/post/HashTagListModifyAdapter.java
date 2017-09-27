package com.obppamanse.honsulnamnye.post;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DatabaseReference;
import com.obppamanse.honsulnamnye.R;

/**
 * Created by raehyeong.park on 2017. 9. 25..
 */

public class HashTagListModifyAdapter extends FirebaseRecyclerAdapter<String, HashTagListModifyAdapter.HashTagViewHolder> {


    public HashTagListModifyAdapter(ObservableSnapshotArray<String> dataSnapshots) {
        super(dataSnapshots, R.layout.item_hash_tag, HashTagViewHolder.class);
    }

    @Override
    protected void populateViewHolder(HashTagViewHolder viewHolder, String model, int position) {
        viewHolder.setData(model, getRef(position));
    }

    public static class HashTagViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHashTag;

        public HashTagViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(String tag, final DatabaseReference reference) {

            tvHashTag = itemView.findViewById(R.id.btn_hash_tag);
            tvHashTag.setText(tag);
            tvHashTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reference.removeValue();
                }
            });
        }
    }
}
