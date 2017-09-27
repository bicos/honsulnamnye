package com.obppamanse.honsulnamnye.post.write;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.obppamanse.honsulnamnye.databinding.ActivityPostWriteBinding;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostWriteActivity extends AppCompatActivity implements PostContract.WriteView {

    private static final int REQUEST_SELECT_LOCATION = 1000;

    private static final int REQUEST_UPLOAD_IMAGE = 1001;

    private ActivityPostWriteBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_write);

        binding.setViewModel(new PostWriteViewModel(this, new PostWriteModel()));
        binding.loadingProgress.hide();
    }

    @Override
    public void successWritePost() {
        Toast.makeText(getApplicationContext(), "글쓰기를 완료하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failureWritePost(Exception e) {
        Toast.makeText(getApplicationContext(), "글쓰기를 실패하였습니다.\n[" + e.getMessage() + "]", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorWrongDueDate() {
        Toast.makeText(getApplicationContext(), R.string.error_invalid_due_date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startSelectLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Place place = binding.getViewModel().getPost().getPlace();

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
    public void showErrorHashTagEmpty() {
        Toast.makeText(this, "해쉬 태그를 입력해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_LOCATION && resultCode == Activity.RESULT_OK && data != null) {
            com.google.android.gms.location.places.Place place = PlacePicker.getPlace(this, data);
            if (place != null) {
                showInputAddressName(place);
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_select_place, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_UPLOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            binding.getViewModel().addUploadImageUri(data.getData());
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
}
