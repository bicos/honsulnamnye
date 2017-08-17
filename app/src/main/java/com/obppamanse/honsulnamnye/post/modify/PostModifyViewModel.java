package com.obppamanse.honsulnamnye.post.modify;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public void clickDueDate(final Context context) {
        Calendar prevSelectDate = Calendar.getInstance();

        if (model.getDueDate() != 0L) {
            prevSelectDate.setTimeInMillis(model.getDueDate());
        }

        final Calendar selectDate = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar today = Calendar.getInstance();
                selectDate.set(i, i1, i2);

                if (today.after(selectDate)) {
                    view.showErrorWrongDueDate();
                    return;
                }

                model.setDueDate(selectDate.getTimeInMillis());
                notifyPropertyChanged(BR.dueDateTxt);
            }
        }, prevSelectDate.get(Calendar.YEAR), prevSelectDate.get(Calendar.MONTH), prevSelectDate.get(Calendar.DAY_OF_MONTH));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                showTimePicker(context, selectDate);
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

                model.setDueDate(calendar.getTimeInMillis());
                notifyPropertyChanged(BR.dueDateTxt);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    public void clickSelectLocation() {
        view.startSelectLocation();
    }

    public void clickModifyPost(Context context) {
        view.showProgress();

        Activity activity = ActivityUtils.getActivity(context);
        if (activity == null) {
            return;
        }

        model.modifyPost().addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                view.dismissProgress();
                view.successModifyPost();
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "post modify error", e);
                view.dismissProgress();
                view.failureModifyPost();
            }
        });
    }
}
