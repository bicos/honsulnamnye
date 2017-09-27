package com.obppamanse.honsulnamnye.post.modify;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.databinding.ActivityPostModifyBinding;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

/**
 * Created by raehyeong.park on 2017. 7. 11..
 */

public class PostModifyActivity extends AppCompatActivity implements PostContract.ModifyView {

    private static final int IMAGE_UPLOAD_PROGRESS_ID = 1;

    private static final String CHANEL_IMAGE_UPLOAD_PROGRESS = "image_upload_progress";

    public static final String PARAM_POST = "post";

    private static final int REQUEST_SELECT_LOCATION = 1000;

    private static final int REQUEST_UPLOAD_IMAGE = 1001;

    private ActivityPostModifyBinding binding;

    private NotificationManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Post post;

        Intent intent = getIntent();
        if (intent != null) {
            post = intent.getParcelableExtra(PARAM_POST);
        } else {
            failureModifyPost();
            finish();
            return;
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_modify);
        PostContract.ModifyModel model = new PostModifyModel(post);
        binding.setViewModel(new PostModifyViewModel(model, this));

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showProgress() {
        binding.loadingProgress.show();
    }

    @Override
    public void dismissProgress() {
        binding.loadingProgress.hide();
    }

    @Override
    public void successModifyPost() {
        Toast.makeText(this, R.string.success_modify_post, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failureModifyPost() {
        Toast.makeText(this, R.string.failure_modify_post, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorWrongDueDate() {
        Toast.makeText(getApplicationContext(), R.string.error_invalid_due_date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startSelectLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Place place = binding.getViewModel().getPlace();

        if (place != null) {
            builder.setLatLngBounds(
                    LatLngBounds.builder().include(new LatLng(
                            place.getLat(),
                            place.getLon())).build());
        }

        try {
            startActivityForResult(builder.build(this), REQUEST_SELECT_LOCATION);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chooseUploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_select_image)), REQUEST_UPLOAD_IMAGE);
    }

    @Override
    public void failureUploadImage() {
        Toast.makeText(getApplicationContext(), R.string.error_failed_upload_image, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successUploadImage(Uri data) {
        if (data != null) {
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    new Intent(Intent.ACTION_VIEW, data),
                    FLAG_ONE_SHOT
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_IMAGE_UPLOAD_PROGRESS)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.msg_upload_image))
                    .setContentText(getString(R.string.msg_upload_image_complete))
                    .setProgress(0, 0, false)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true);

            manager.notify(IMAGE_UPLOAD_PROGRESS_ID, builder.build());
        }

        Toast.makeText(getApplicationContext(), R.string.success_image_upload, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUploadProgress(long totalByteCount, long bytesTransferred) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_IMAGE_UPLOAD_PROGRESS)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.msg_upload_image))
                .setContentText(totalByteCount + "/" + bytesTransferred)
                .setProgress((int) totalByteCount, (int) bytesTransferred, false);

        manager.notify(IMAGE_UPLOAD_PROGRESS_ID, builder.build());
    }

    @Override
    public void failureDeleteImage() {
        Toast.makeText(getApplicationContext(), R.string.failure_image_delete, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successDeleteImage() {
        Toast.makeText(getApplicationContext(), R.string.success_image_delete, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorHashTagEmpty() {
        Toast.makeText(getApplicationContext(), R.string.error_hash_tag_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_LOCATION && resultCode == Activity.RESULT_OK && data != null) {
            com.google.android.gms.location.places.Place place = PlacePicker.getPlace(this, data);
            if (place != null) {
                showInputAddressName(place);
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_select_place, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_UPLOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            binding.getViewModel().uploadImage(this, data.getData());
        }
    }

    private void showInputAddressName(final com.google.android.gms.location.places.Place place) {
        FrameLayout container = new FrameLayout(this);
        container.setPadding(getResources().getDimensionPixelSize(R.dimen.default_margin),
                0,
                getResources().getDimensionPixelSize(R.dimen.default_margin),
                getResources().getDimensionPixelSize(R.dimen.default_margin));

        final EditText editText = new EditText(this);
        editText.setHint(R.string.hint_input_address_name);
        editText.setText(place.getName());
        container.addView(editText);

        new AlertDialog.Builder(this)
                .setTitle(R.string.title_input_address_name)
                .setView(container)
                .setMessage(R.string.msg_input_address_name)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String addressName = editText.getText().toString();
                        if (!TextUtils.isEmpty(addressName)) {
                            binding.getViewModel().updatePlace(new Place(addressName,
                                    place.getLatLng().latitude,
                                    place.getLatLng().longitude));
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_address_name_empty, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    public static void start(Activity activity, Post post) {
        Intent intent = new Intent(activity, PostModifyActivity.class);
        intent.putExtra(PARAM_POST, post);
        activity.startActivity(intent);
    }
}
