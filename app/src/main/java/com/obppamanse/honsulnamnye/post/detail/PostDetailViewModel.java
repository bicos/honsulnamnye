package com.obppamanse.honsulnamnye.post.detail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.obppamanse.honsulnamnye.BR;
import com.obppamanse.honsulnamnye.chat.ChatActivity;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.main.model.Category;
import com.obppamanse.honsulnamnye.post.HashTagListModifyAdapter;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.util.ActivityUtils;
import com.obppamanse.honsulnamnye.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Ravy on 2017. 6. 11..
 */

public class PostDetailViewModel extends BaseObservable {

    private PostContract.DetailView view;

    private PostContract.DetailModel model;

    private boolean isMember;

    private String categoryName;

    public PostDetailViewModel(final PostContract.DetailView view, PostContract.DetailModel model) {
        this.view = view;
        this.model = model;

        view.showProgress();

        model.requestIsMember(isMember -> {
            view.dismissProgress();
            PostDetailViewModel.this.isMember = isMember;
            notifyPropertyChanged(BR.isMember);
        });

        model.requestCategoryName(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Category category = dataSnapshot.getValue(Category.class);
                if (category != null) {
                    setCategoryName(category.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        List<StorageReference> list = new ArrayList<>();

        for (String url : model.getFileNames()) {
            list.add(FirebaseUtils.getPostStorageRef(model.getPostKey()).child(url));
        }

        return list;
    }

    @Bindable
    public String getChatKey() {
        return model != null ? model.getChatKey() : null;
    }

    @Bindable
    public DatabaseReference getHashTagRef() {
        return FirebaseUtils.getPostRef().child(model.getPostKey()).child(FirebaseUtils.POST_HASH_TAG_REF);
    }

    public void clickDeletePost(Activity activity) {
        try {
            model.deletePost(activity, task -> {
                if (task.isSuccessful()) {
                    view.successDeletePost();
                } else {
                    view.failureDeletePost(task.getException());
                }
            });
        } catch (Exception e) {
            view.failureDeletePost(e);
        }
    }

    public void clickJoinGroup(Context context) {
        try {
            model.joinGroup((Activity) context, task -> {
                if (task.isSuccessful()) {
                    isMember = true;
                    view.successJoinGroup();
                } else {
                    isMember = false;
                    view.failureJoinGroup(task.getException());
                }
                notifyChange();
            });
        } catch (Exception e) {
            view.failureJoinGroup(e);
        }
    }

    public void clickWithdrawalGroup(final Context context) {
        view.showAlertWithdrawalGroup((dialogInterface, i) -> {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                try {
                    model.withdrawalGroup((Activity) context, task -> {

                        if (task.isSuccessful()) {
                            isMember = false;
                            view.successWithdrawalGroup();
                        } else {
                            isMember = true;
                            view.failureWithdrawalGroup(task.getException());
                        }
                        notifyChange();

                    });
                } catch (Exception e) {
                    view.failureJoinGroup(e);
                }
            }
        });
    }

    public void clickJoinChatRoom(Context context) {
        final Activity activity = ActivityUtils.getActivity(context);
        if (activity == null) {
            return;
        }

        if (TextUtils.isEmpty(model.getChatKey())) {
            view.showAlertCreateChatRoom((dialogInterface, i) -> model.createChatRoom()
                    .addOnSuccessListener(activity, chatKey -> ChatActivity.start(activity, chatKey))
                    .addOnFailureListener(activity, e -> view.failureJoinChatRoom(e)));
        } else {
            model.joinChatRoom(activity, chatKey -> ChatActivity.start(activity, chatKey), e -> view.failureJoinChatRoom(e));
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

    @BindingAdapter("setModifyHashTagList")
    public static void setModifyHashTagList(RecyclerView recyclerView, DatabaseReference hashTagRef) {
        if (recyclerView != null && recyclerView.getAdapter() == null && hashTagRef != null) {
            LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext());
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);

            HashTagListModifyAdapter adapter = new HashTagListModifyAdapter(hashTagRef, false);
            recyclerView.setAdapter(adapter);
        }
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        notifyPropertyChanged(BR.categoryName);
    }

    @Bindable
    public String getCategoryName() {
        return categoryName;
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
