package com.obppamanse.honsulnamnye.post.modify;

import android.app.Activity;
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
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.SimpleImageAdapter;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.util.ActivityUtils;
import com.obppamanse.honsulnamnye.util.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by raehyeong.park on 2017. 7. 11..
 */

public class PostModifyViewModel extends BaseObservable implements SimpleImageAdapter.RemoveItemListener<StorageReference> {

    private static final String TAG = "PostModifyViewModel";

    private PostContract.ModifyModel model;

    private PostContract.ModifyView view;

    private List<StorageReference> uploadList;

    public PostModifyViewModel(PostContract.ModifyModel model, PostContract.ModifyView view) {
        this.model = model;
        this.view = view;
        initUriList();
    }

    private void initUriList() {
        uploadList = new ArrayList<>();

        for (String fileName : model.getFileNames()) {
            uploadList.add(FirebaseUtils.getPostStorageRef(model.getPostKey()).child(fileName));
        }
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

    @Bindable
    public List<StorageReference> getUploadList() {
        return uploadList;
    }

    @Bindable
    public String getHashTag() {
        return model.getHashTag();
    }

    @Bindable
    public void setHashTag(String hashTag) {
        model.setHashTag(hashTag);
    }

    @Bindable
    public List<String> getHashTagList(){
        return model.getHashTagList();
    }

    public SimpleImageAdapter.RemoveItemListener<StorageReference> getRemoveListener() {
        return this;
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

    public void clickAddHashTag(){
        if (TextUtils.isEmpty(model.getHashTag())) {
            view.showErrorHashTagEmpty();
            return;
        }

        model.getHashTagList().add(model.getHashTag());
        notifyPropertyChanged(BR.hashTagList);

        model.setHashTag("");
        notifyPropertyChanged(BR.hashTag);
    }

    public void clickDueDate(final Context context) {
        final Calendar prevSelectDate = Calendar.getInstance();

        if (model.getDueDate() != 0L) {
            prevSelectDate.setTimeInMillis(model.getDueDate());
        }

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar today = Calendar.getInstance();
                prevSelectDate.set(i, i1, i2);

                if (today.after(prevSelectDate)) {
                    view.showErrorWrongDueDate();
                    return;
                }

                model.setDueDate(prevSelectDate.getTimeInMillis());
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

    public void clickUploadFile() {
        view.chooseUploadImage();
    }

    public void uploadImage(Activity activity, @NonNull final Uri data) {
        final String postImageName = model.getPostKey() + "_image_" + System.currentTimeMillis();
        final StorageReference reference = FirebaseUtils.getPostStorageRef(model.getPostKey()).child(postImageName);

        view.showProgress();

        reference.putFile(data)
                .addOnProgressListener(activity, new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        view.showUploadProgress(taskSnapshot.getTotalByteCount(), taskSnapshot.getBytesTransferred());
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (task.isSuccessful()) {
                            model.getFileNames().add(postImageName); // model data
                            return model.modifyUploadImage(); // start update model
                        }

                        throw task.getException();
                    }
                })
                .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        uploadList.add(reference); // adapter data
                        notifyPropertyChanged(BR.uploadList);
                        view.dismissProgress();
                        view.successUploadImage(data);
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "upload image failed ", e);
                        reference.delete();
                        view.dismissProgress();
                        view.failureUploadImage();
                    }
                });
    }

    @Override
    public void onItemRemoved(final StorageReference ref) {
        view.showProgress();
        ref.delete().continueWithTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                if (task.isSuccessful()) {
                    model.removeFileName(ref.getName());
                    return model.modifyUploadImage();
                } else {
                    return task;
                }
            }
        }).addOnSuccessListener(view.getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                view.dismissProgress();
                view.successDeleteImage();
            }
        }).addOnFailureListener(view.getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.dismissProgress();
                view.failureDeleteImage();
            }
        });
    }

    @BindingAdapter({"setUploadImageList", "setRemoveListener"})
    public static void setUploadImageList(RecyclerView recyclerView, List<StorageReference> uploadImageList, SimpleImageAdapter.RemoveItemListener<StorageReference> listener) {
        if (uploadImageList == null || listener == null) {
            return;
        }

        if (recyclerView.getAdapter() == null) {
            SimpleImageAdapter<StorageReference> adapter = new SimpleImageAdapter<>(uploadImageList);
            adapter.setRemoveItemListener(listener);
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } else {
            ((SimpleImageAdapter<StorageReference>) recyclerView.getAdapter()).setDataList(uploadImageList);
        }
    }
}
