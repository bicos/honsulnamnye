package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.util.DateUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostDetailViewModel extends BaseObservable {

    private PostContract.DetailView view;

    private PostContract.DetailModel model;

    private boolean isMember;

    public PostDetailViewModel(final PostContract.DetailView view, PostContract.DetailModel model) {
        this.view = view;
        this.model = model;

        view.showProgress();
        model.requestIsMember((Activity) view.getContext(), new PostDetailModel.MemberExistListener() {
            @Override
            public void isMember(boolean isMember) {
                view.dismissProgress();
                PostDetailViewModel.this.isMember = isMember;
                notifyPropertyChanged(BR.isMember);
            }
        });
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
    public boolean getIsWriter() {
        return model.isWriter();
    }

    @Bindable
    public boolean getIsMember() {
        return isMember;
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
    public DatabaseReference getParticipantListRef() {
        return FirebaseUtils.getParticipantListRef(model.getPostKey());
    }

    @Bindable
    public Place getPlace() {
        return model.getPlace();
    }

    @Bindable
    public List<StorageReference> getImageList() {
        return model.getImageUrlList();
    }

    public void clickDeletePost(Activity activity) {
        try {
            model.deletePost(activity, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        view.successDeletePost();
                    } else {
                        view.failureDeletePost(task.getException());
                    }
                }
            });
        } catch (Exception e) {
            view.failureDeletePost(e);
        }
    }

    public void clickJoinGroup(Context context) {
        try {
            model.joinGroup((Activity) context, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        isMember = true;
                        view.successJoinGroup();
                    } else {
                        isMember = false;
                        view.failureJoinGroup(task.getException());
                    }
                    notifyChange();
                }
            });
        } catch (Exception e) {
            view.failureJoinGroup(e);
        }
    }

    public void clickWithdrawalGroup(Context context) {
        try {
            model.withdrawalGroup((Activity) context, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        isMember = false;
                        view.successWithdrawalGroup();
                    } else {
                        isMember = true;
                        view.failureWithdrawalGroup(task.getException());
                    }
                    notifyChange();
                }
            });
        } catch (Exception e) {
            view.failureJoinGroup(e);
        }
    }

    public void updateGoogleMap(GoogleMap googleMap) {
        Place place = model.getPlace();
        if (place != null) {
            LatLng latLng = new LatLng(place.getLat(), place.getLon());
            googleMap.getUiSettings().setAllGesturesEnabled(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));
            googleMap.addMarker(new MarkerOptions().title(place.getName())
                    .position(latLng).visible(true));
        }
    }

    @BindingAdapter("setParticipantList")
    public static void setParticipantList(RecyclerView recyclerView, PostDetailViewModel viewModel) {
        if (viewModel == null || viewModel.getParticipantListRef() == null) {
            return;
        }

        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (adapter == null) {
            recyclerView.setAdapter(new ParticipantListAdapter(viewModel.getView(), viewModel.getParticipantListRef()));
        }
    }

    private PostContract.DetailView getView() {
        return view;
    }

    @BindingAdapter("setImageList")
    public static void setImageList(ViewPager pager, List<StorageReference> imgUrlList) {
        if (imgUrlList == null) {
            return;
        }

        if (pager.getAdapter() == null) {
            ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(imgUrlList);
            pager.setAdapter(adapter);
        } else {
            ImageViewPagerAdapter adapter = (ImageViewPagerAdapter) pager.getAdapter();
            adapter.setImageUrlList(imgUrlList);
        }
    }

    private static class ImageViewPagerAdapter extends PagerAdapter {

        private List<StorageReference> imageUrlList;

        public ImageViewPagerAdapter(@NonNull List<StorageReference> imageUrlList) {
            this.imageUrlList = imageUrlList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            Glide.with(container.getContext())
                    .using(new FirebaseImageLoader())
                    .load(imageUrlList.get(position))
                    .bitmapTransform(new BlurTransformation(container.getContext()))
                    .centerCrop()
                    .into(imageView);
            container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        @Override
        public int getCount() {
            return imageUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public void setImageUrlList(List<StorageReference> imageUrlList) {
            this.imageUrlList = imageUrlList;
            notifyDataSetChanged();
        }
    }
}
