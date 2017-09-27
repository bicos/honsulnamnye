package com.obppamanse.honsulnamnye.post;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.obppamanse.honsulnamnye.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raehyeong.park on 2017. 9. 26..
 */

public class SimpleHashTagAdapter extends RecyclerView.Adapter<SimpleHashTagAdapter.SimpleHashTagViewHolder> {

    private List<String> hashTagList;

    public SimpleHashTagAdapter(List<String> hashTagList) {
        this.hashTagList = hashTagList;
    }

    @Override
    public SimpleHashTagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleHashTagViewHolder(parent, this);
    }

    @Override
    public void onBindViewHolder(SimpleHashTagViewHolder holder, int position) {
        if (hashTagList != null) {
            holder.populateHashTag(hashTagList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return hashTagList != null ? hashTagList.size() : 0;
    }

    public void addItem(String hashTag) {
        if (hashTagList == null) {
            return;
        }

        hashTagList.add(hashTag);
        notifyItemInserted(hashTagList.size());
    }

    public void removeItem(int index) {
        if (hashTagList == null) {
            return;
        }

        hashTagList.remove(index);
        notifyItemRemoved(index);
    }

    public void setDataList(List<String> dataList) {
        this.hashTagList = dataList;
        notifyDataSetChanged();
    }

    public static class SimpleHashTagViewHolder extends RecyclerView.ViewHolder {

        Button btnHashTag;

        String hashTag;

        public SimpleHashTagViewHolder(ViewGroup viewGroup, final SimpleHashTagAdapter adapter) {
            super(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_hash_tag, viewGroup, false));

            btnHashTag = itemView.findViewById(R.id.btn_hash_tag);
            btnHashTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(hashTag)) {
                        adapter.removeItem(getAdapterPosition());
                    }
                }
            });
        }

        public void populateHashTag(String hashTag) {
            this.hashTag = hashTag;
            btnHashTag.setText(hashTag);
        }
    }

}
