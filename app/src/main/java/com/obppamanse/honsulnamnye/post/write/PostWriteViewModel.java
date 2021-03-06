package com.obppamanse.honsulnamnye.post.write;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Spinner;

import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.post.CategorySelectAdapter;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.SimpleHashTagAdapter;
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

    @Bindable
    public List<String> getHashTagList() {
        return mModel.getHashTagList();
    }

    @Bindable
    public String getHashTag(){
        return mModel.getHashTag();
    }

    @Bindable
    public void setHashTag(String hashTag){
        mModel.setHashTag(hashTag);
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

    public void clickAddHashTag() {
        if (TextUtils.isEmpty(mModel.getHashTag())){
            mView.showErrorHashTagEmpty();
            return;
        }

        mModel.getHashTagList().add(mModel.getHashTag());
        notifyPropertyChanged(BR.hashTagList);

        mModel.setHashTag("");
        notifyPropertyChanged(BR.hashTag);
    }

    public void clickWritePost(Context context) {
        mView.showProgress();

        mModel.writePost(ActivityUtils.getActivity(context), (aVoid) -> {
            mView.dismissProgress();
            mView.successWritePost();
        }, e -> {
            mView.dismissProgress();
            mView.failureWritePost(e);
        });
    }

    public void clickDueDate(final Context context) {
        final Calendar prevSelectDate = Calendar.getInstance();

        if (mModel.getPost().getDueDateTime() != 0L) {
            prevSelectDate.setTimeInMillis(mModel.getPost().getDueDateTime());
        }

        DatePickerDialog dialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
            Calendar today = Calendar.getInstance();
            prevSelectDate.set(i, i1, i2);

            if (today.after(prevSelectDate)) {
                mView.showErrorWrongDueDate();
                return;
            }

            mModel.setDueDate(prevSelectDate.getTimeInMillis());
            notifyPropertyChanged(BR.dueDateTxt);
        }, prevSelectDate.get(Calendar.YEAR), prevSelectDate.get(Calendar.MONTH), prevSelectDate.get(Calendar.DAY_OF_MONTH));
        dialog.setOnDismissListener(dialogInterface -> showTimePicker(context, prevSelectDate));
        dialog.show();
    }

    private void showTimePicker(Context context, final Calendar calendar) {
        TimePickerDialog dialog = new TimePickerDialog(context, (timePicker, i, i1) -> {
            calendar.set(Calendar.HOUR_OF_DAY, i);
            calendar.set(Calendar.MINUTE, i1);
            mModel.setDueDate(calendar.getTimeInMillis());
            notifyPropertyChanged(BR.dueDateTxt);
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
            SimpleImageAdapter<Uri> adapter = new SimpleImageAdapter<>(uploadImageList);
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } else {
            ((SimpleImageAdapter<Uri>)recyclerView.getAdapter()).setDataList(uploadImageList);
        }
    }

    @BindingAdapter("setHashTagList")
    public static void setHashTagList(RecyclerView recyclerView, List<String> hashTags) {
        if (hashTags == null) {
            return;
        }

        if (recyclerView.getAdapter() == null) {
            SimpleHashTagAdapter adapter = new SimpleHashTagAdapter(hashTags);
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } else {
            ((SimpleHashTagAdapter)recyclerView.getAdapter()).setDataList(hashTags);
        }
    }

    @BindingAdapter("setCategoryList")
    public static void setCategoryList(Spinner spinner, PostWriteViewModel viewModel) {
        if (spinner.getAdapter() == null) {
            CategorySelectAdapter adapter = new CategorySelectAdapter(spinner.getContext(), viewModel.getWriteModel());
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(adapter);
        }
    }

    private PostContract.WriteModel getWriteModel() {
        return mModel;
    }

    public Post getPost() {
        return mModel.getPost();
    }
}
