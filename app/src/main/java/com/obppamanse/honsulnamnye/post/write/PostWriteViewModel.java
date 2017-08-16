package com.obppamanse.honsulnamnye.post.write;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.util.ActivityUtils;
import com.obppamanse.honsulnamnye.util.DateUtils;
import com.obppamanse.honsulnamnye.util.UiUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by raehyeong.park on 2017. 5. 26..
 */

public class PostWriteViewModel extends BaseObservable {

    private PostContract.WriteView mView;

    private PostContract.WriteModel mModel;

    public PostWriteViewModel(PostContract.WriteView view, PostContract.WriteModel model) {
        this.mView = view;
        this.mModel = model;
    }

    @Bindable
    public String getDueDateTxt() {
        return mModel.getDueDate() != 0L ? DateUtils.getDateStr(mModel.getDueDate()) : "만날 날짜를 선택하여 주세요";
    }

    @Bindable
    public String getPlaceName() {
        return mModel.getPlaceName() != null ? mModel.getPlaceName() : "만날 장소를 선택하여 주세요.";
    }

    @Bindable
    public List<Uri> getUploadImageUri() {
        return mModel.getUploadImageUri();
    }

    public void updateTitle(CharSequence title) {
        if (title != null) {
            mModel.setTitle(title.toString());
        }
    }

    public void updateDesc(CharSequence desc) {
        if (desc != null) {
            mModel.setDesc(desc.toString());
        }
    }

    public void updateDueDate(long timeMill) {
        mModel.setDueDate(timeMill);
    }

    public void updatePlace(Place place) {
        mModel.setPlace(place);
        notifyPropertyChanged(BR.placeName);
    }

    public void clickWritePost(Context context) {
        mView.showProgress();

        mModel.writePost(ActivityUtils.getActivity(context), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mView.dismissProgress();
                mView.successWritePost();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mView.dismissProgress();
                mView.failureWritePost(e);
            }
        });
    }

    public void clickDueDate(Context context) {
        Calendar prevSelectDate = Calendar.getInstance();

        if (mModel.getDueDate() != 0L) {
            prevSelectDate.setTimeInMillis(mModel.getDueDate());
        }

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar today = Calendar.getInstance();
                Calendar selectDate = Calendar.getInstance();
                selectDate.set(i, i1, i2);

                if (today.after(selectDate)) {
                    mView.showErrorWrongDueDate();
                    return;
                }

                mModel.setDueDate(selectDate.getTimeInMillis());
                notifyPropertyChanged(BR.dueDateTxt);
            }
        }, prevSelectDate.get(Calendar.YEAR), prevSelectDate.get(Calendar.MONTH), prevSelectDate.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void clickSelectLocation() {
        mView.startSelectLocation();
    }

    public void clickUploadFile() {
        mView.chooseUploadImage();
    }

    public void addUploadImageUri(Uri data) {
        mModel.addUploadImageUri(data);
        notifyPropertyChanged(BR.uploadImageUri);
    }

    @BindingAdapter("setUploadImageList")
    public static void setUploadImageList(RecyclerView recyclerView, List<Uri> uploadImageList) {
        if (uploadImageList == null) {
            return;
        }

        if (recyclerView.getAdapter() == null) {
            SimpleImageAdapter adapter = new SimpleImageAdapter(uploadImageList);
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } else {
            ((SimpleImageAdapter)recyclerView.getAdapter()).setUriList(uploadImageList);
        }
    }

    private static class SimpleImageAdapter extends RecyclerView.Adapter<SimpleImageViewHolder> {

        List<Uri> uriList;

        public SimpleImageAdapter(List<Uri> uriList) {
            this.uriList = uriList;
        }

        public void setUriList(List<Uri> uriList) {
            this.uriList = uriList;
            notifyDataSetChanged();
        }

        @Override
        public SimpleImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(parent.getContext());
            int padding = (int) UiUtils.dpToPx(parent.getContext(), 5);
            imageView.setPadding(padding, 0, padding, 0);
            return new SimpleImageViewHolder(imageView, this);
        }

        @Override
        public void onBindViewHolder(SimpleImageViewHolder holder, int position) {
            holder.loadImage(uriList.get(position));
        }

        @Override
        public int getItemCount() {
            return uriList.size();
        }

        public void removeItem(int position) {
            uriList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private static class SimpleImageViewHolder extends RecyclerView.ViewHolder{

        public SimpleImageViewHolder(View itemView, final SimpleImageAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupView(adapter, v, getAdapterPosition());
                }
            });
        }

        public void loadImage(Uri uri) {
            Glide.with(itemView.getContext())
                    .load(uri)
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
}
