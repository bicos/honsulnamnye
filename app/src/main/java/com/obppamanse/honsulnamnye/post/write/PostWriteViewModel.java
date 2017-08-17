package com.obppamanse.honsulnamnye.post.write;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.SimpleImageAdapter;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;
import com.obppamanse.honsulnamnye.util.ActivityUtils;
import com.obppamanse.honsulnamnye.util.DateUtils;

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
        return mModel.getPost().getDueDateTime() != 0L ?
                DateUtils.getDateStr(mModel.getPost().getDueDateTime()) :
                "만날 날짜를 선택하여 주세요";
    }

    @Bindable
    public String getPlaceName() {
        String placeName = null;

        if (mModel.getPost().getPlace() != null) {
            placeName = mModel.getPost().getPlace().getName();
        }

        return TextUtils.isEmpty(placeName) ? "만날 장소를 선택하여 주세요." : placeName;
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

    public void clickDueDate(final Context context) {
        final Calendar prevSelectDate = Calendar.getInstance();

        if (mModel.getPost().getDueDateTime() != 0L) {
            prevSelectDate.setTimeInMillis(mModel.getPost().getDueDateTime());
        }

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar today = Calendar.getInstance();
                prevSelectDate.set(i, i1, i2);

                if (today.after(prevSelectDate)) {
                    mView.showErrorWrongDueDate();
                    return;
                }

                mModel.setDueDate(prevSelectDate.getTimeInMillis());
                notifyPropertyChanged(BR.dueDateTxt);
            }
        }, prevSelectDate.get(Calendar.YEAR), prevSelectDate.get(Calendar.MONTH), prevSelectDate.get(Calendar.DAY_OF_MONTH));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                showTimePicker(context, prevSelectDate);
            }
        });
        dialog.show();
    }

    private void showTimePicker(Context context, final Calendar calendar) {
        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                mModel.setDueDate(calendar.getTimeInMillis());
                notifyPropertyChanged(BR.dueDateTxt);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
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
            ((SimpleImageAdapter)recyclerView.getAdapter()).setDataList(uploadImageList);
        }
    }

    public Post getPost() {
        return mModel.getPost();
    }
}
