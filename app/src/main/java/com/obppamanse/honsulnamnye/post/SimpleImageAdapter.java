package com.obppamanse.honsulnamnye.post;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.util.UiUtils;

import java.util.List;

/**
 * Created by raehyeong.park on 2017. 8. 17..
 */

public class SimpleImageAdapter<T> extends RecyclerView.Adapter<SimpleImageAdapter.SimpleImageViewHolder<T>> {

    private List<T> uriList;

    private RemoveItemListener<T> removeItemListener;

    public SimpleImageAdapter(List<T> uriList) {
        this.uriList = uriList;
    }

    public void setDataList(List<T> uriList) {
        this.uriList = uriList;
        notifyDataSetChanged();
    }

    public void setRemoveItemListener(RemoveItemListener<T> removeItemListener) {
        this.removeItemListener = removeItemListener;
    }

    @Override
    public SimpleImageViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        int padding = (int) UiUtils.dpToPx(parent.getContext(), 5);
        imageView.setPadding(padding, 0, padding, 0);
        return new SimpleImageViewHolder<T>(imageView, this);
    }

    @Override
    public void onBindViewHolder(SimpleImageViewHolder<T> holder, int position) {
        holder.loadImage(uriList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public void removeItem(int position) {
        T removeItem = uriList.remove(position);
        notifyItemRemoved(position);
        if (removeItemListener != null && removeItem != null) {
            removeItemListener.onItemRemoved(removeItem);
        }
    }

    static class SimpleImageViewHolder<T> extends RecyclerView.ViewHolder {

        public SimpleImageViewHolder(View itemView, final SimpleImageAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupView(adapter, v, getAdapterPosition());
                }
            });
        }

        public void loadImage(T data) {
            Glide.with(itemView.getContext())
                    .load(data)
                    .override((int) UiUtils.dpToPx(itemView.getContext(), 100), (int) UiUtils.dpToPx(itemView.getContext(), 100))
                    .into((ImageView) itemView);
        }

        private void showPopupView(final SimpleImageAdapter adapter, View view, final int position) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.menu_upload_image);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_delete) {
                        adapter.removeItem(position);
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }

    public interface RemoveItemListener<T> {
        void onItemRemoved(T item);
    }
}
