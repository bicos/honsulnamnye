package com.obppamanse.honsulnamnye.post.write;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.adapters.DatePickerBindingAdapter;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Location;
import com.obppamanse.honsulnamnye.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
        return mModel.getDueDateTxt();
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

    public void updateLocation(Location location) {
        mModel.setLocation(location);
    }

    public void clickWritePost(View view) {
        try {
            mModel.writePost((Activity) view.getContext(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mView.successWritePost();
                    } else {
                        mView.failureWritePost(task.getException());
                    }
                }
            });
        } catch (Exception e) {
            mView.failureWritePost(e);
        }
    }

    public void clickDueDate(Context context) {
        Calendar prevSelectDate = mModel.getDueDateCalendar() != null ?
                mModel.getDueDateCalendar() :
                Calendar.getInstance();

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


                mModel.setDueDateTxt(DateUtils.getDateStr(selectDate.getTimeInMillis()));
                mModel.setDueDate(selectDate.getTimeInMillis());
                notifyPropertyChanged(BR.dueDateTxt);
            }
        }, prevSelectDate.get(Calendar.YEAR), prevSelectDate.get(Calendar.MONTH), prevSelectDate.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
