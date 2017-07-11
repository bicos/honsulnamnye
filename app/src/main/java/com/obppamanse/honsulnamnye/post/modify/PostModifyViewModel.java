package com.obppamanse.honsulnamnye.post.modify;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.util.ActivityUtils;
import com.obppamanse.honsulnamnye.util.DateUtils;

import java.util.Calendar;


/**
 * Created by raehyeong.park on 2017. 7. 11..
 */

public class PostModifyViewModel extends BaseObservable {

    private static final String TAG = "PostModifyViewModel";

    private PostContract.ModifyModel model;

    private PostContract.ModifyView view;

    public PostModifyViewModel(PostContract.ModifyModel model, PostContract.ModifyView view) {
        this.model = model;
        this.view = view;
    }

    @Bindable
    public String getTitle() {
        return model.getTitle();
    }

    @Bindable
    public String getDesc() {
        return model.getDesc();
    }

    @Bindable
    public Place getPlace() {
        return model.getPlace();
    }

    @Bindable
    public String getDueDateTxt() {
        if (model.getDueDate() == 0L) {
            return "미정";
        } else {
            return DateUtils.getDateStr(model.getDueDate());
        }
    }

    @Bindable
    public String getPlaceName() {
        return getPlace() != null ? getPlace().getName() : "미정";
    }

    public void updateTitle(String title) {
        model.setTitle(title);
        notifyPropertyChanged(BR.title);
    }

    public void updateDesc(String desc) {
        model.setDesc(desc);
        notifyPropertyChanged(BR.desc);
    }

    public void updatePlace(Place place) {
        model.setPlace(place);
        notifyPropertyChanged(BR.place);
        notifyPropertyChanged(BR.placeName);
    }

    public void clickDueDate(Context context) {
        Calendar prevSelectDate = Calendar.getInstance();

        if (model.getDueDate() != 0L) {
            prevSelectDate.setTimeInMillis(model.getDueDate());
        }

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar today = Calendar.getInstance();
                Calendar selectDate = Calendar.getInstance();
                selectDate.set(i, i1, i2);

                if (today.after(selectDate)) {
                    view.showErrorWrongDueDate();
                    return;
                }

                model.setDueDate(selectDate.getTimeInMillis());
                notifyPropertyChanged(BR.dueDateTxt);
            }
        }, prevSelectDate.get(Calendar.YEAR), prevSelectDate.get(Calendar.MONTH), prevSelectDate.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void clickSelectLocation() {
        view.startSelectLocation();
    }

    public void clickModifyPost(Context context) {
        view.showProgress();
        try {
            model.modifyPost(ActivityUtils.getActivity(context), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    view.dismissProgress();
                    if (task.isSuccessful()) {
                        view.successModifyPost();
                    } else {
                        Log.e(TAG, task.getException().getMessage());
                        view.failureModifyPost();
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            view.dismissProgress();
            view.failureModifyPost();
        }
    }
}
