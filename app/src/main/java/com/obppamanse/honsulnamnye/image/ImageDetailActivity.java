package com.obppamanse.honsulnamnye.image;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.obppamanse.honsulnamnye.R;

/**
 * Created by raehyeong.park on 2017. 8. 25..
 */

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_detail);

        Uri uri = getIntent().getData();

        if (uri == null) {
            imageLoadError();
            return;
        }

        final PhotoView view = findViewById(R.id.image);
        final ContentLoadingProgressBar progressBar = findViewById(R.id.loading_progress);
        progressBar.show();

        Glide.with(this).load(uri)
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.hide();
                        imageLoadError();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.hide();
                        return false;
                    }
                })
                .into(view);
    }

    private void imageLoadError() {
        Toast.makeText(getApplicationContext(), "이미지를 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public static void start(Context context, String url) {
        try {
            Intent intent = new Intent(context, ImageDetailActivity.class);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "이미지를 읽어올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
