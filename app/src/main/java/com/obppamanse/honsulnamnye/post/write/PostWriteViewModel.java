package com.obppamanse.honsulnamnye.post.write;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.util.ActivityUtils;
import com.obppamanse.honsulnamnye.util.DateUtils;

import java.util.Calendar;

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
        try {
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
        } catch (Exception e) {
            mView.dismissProgress();
            mView.failureWritePost(e);
        }
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
        mView.chooseProfileImage();
    }

    public void addUploadImageUri(Uri data) {
        mModel.addUploadImageUri(data);
    }
}
