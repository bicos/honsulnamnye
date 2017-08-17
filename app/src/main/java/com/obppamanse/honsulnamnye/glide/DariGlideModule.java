package com.obppamanse.honsulnamnye.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.module.GlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

/**
 * Created by raehyeong.park on 2017. 8. 17..
 */

public class DariGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(StorageReference.class, InputStream.class, new ModelLoaderFactory<StorageReference, InputStream>() {
            @Override
            public ModelLoader<StorageReference, InputStream> build(Context context, GenericLoaderFactory factories) {
                return new FirebaseImageLoader();
            }

            @Override
            public void teardown() {

            }
        });
    }
}
