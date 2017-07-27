package com.obppamanse.honsulnamnye.post.write;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;
import com.obppamanse.honsulnamnye.post.PostContract;
import com.obppamanse.honsulnamnye.post.model.Place;
import com.obppamanse.honsulnamnye.post.model.Post;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class PostWriteModel implements PostContract.WriteModel {

    private DatabaseReference postRef;

    private Post post;

    private List<Uri> uploadUris;

    public PostWriteModel() {
        postRef = FirebaseUtils.getPostRef();
        post = new Post();
        uploadUris = new ArrayList<>();
    }

    @Override
    public void writePost(final Activity activity, final OnSuccessListener<Void> successListener, final OnFailureListener failureListener) throws Exception {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            throw new PostContract.NotExistAuthUserException();
        }

        if (post == null) {
            throw new PostContract.FailureWritePostException();
        }

        if (TextUtils.isEmpty(post.getTitle())){
            throw new PostContract.EmptyTitlePostException();
        }

        if (TextUtils.isEmpty(post.getDesc())) {
            throw new PostContract.EmptyDescPostException();
        }

        post.setUid(user.getUid());

        final String key = postRef.push().getKey();
        if (TextUtils.isEmpty(key)) {
            throw new PostContract.FailureWritePostException();
        }

        if (uploadUris.isEmpty()) {
            uploadPost(key, activity, successListener, failureListener);
        } else {
            Observable.just(uploadUris)
                    .flatMapIterable(new Function<List<Uri>, Iterable<Uri>>() {
                        @Override
                        public Iterable<Uri> apply(@NonNull List<Uri> uris) throws Exception {
                            return uris;
                        }
                    })
                    .flatMap(new Function<Uri, ObservableSource<UploadTask.TaskSnapshot>>() {
                        @Override
                        public ObservableSource<UploadTask.TaskSnapshot> apply(@NonNull Uri uri) throws Exception {
                            return createFlowUploadImage(activity, key, uri, post);
                        }
                    })
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<UploadTask.TaskSnapshot>>() {
                        @Override
                        public void accept(List<UploadTask.TaskSnapshot> taskSnapshots) throws Exception {
                            uploadPost(key, activity, successListener, failureListener);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            failureListener.onFailure(new Exception(throwable));
                        }
                    });
        }
    }

    private Observable<UploadTask.TaskSnapshot> createFlowUploadImage(final Activity activity, final String key, final Uri uri, final Post post) {
        return Observable.create(new ObservableOnSubscribe<UploadTask.TaskSnapshot>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<UploadTask.TaskSnapshot> e) throws Exception {
                final String fileName = key + "_image_"+System.currentTimeMillis();
                FirebaseUtils.getPostStorageRef(key).child(fileName).putFile(uri).addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        post.addUploadFileName(fileName);
                        e.onNext(taskSnapshot);
                        e.onComplete();
                    }
                }).addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@android.support.annotation.NonNull Exception exception) {
                        e.onError(exception);
                    }
                });
            }
        });
    }

    private void uploadPost(final String key, final Activity activity, final OnSuccessListener<Void> successListener, final OnFailureListener failureListener) {
        post.setKey(key);
        post.setWriteTime(System.currentTimeMillis());

        if (post.getFileNames().isEmpty()) {
            postRef.child(key).setValue(post)
                    .addOnSuccessListener(activity, successListener)
                    .addOnFailureListener(activity, failureListener);
        } else {
            postRef.child(key).setValue(post)
                    .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            postRef.child(key).child("fileNames").setValue(post.getFileNames())
                                    .addOnSuccessListener(activity, successListener)
                                    .addOnFailureListener(failureListener);
                        }
                    })
                    .addOnFailureListener(activity, failureListener);
        }
    }

    @Override
    public String getPlaceName() {
        return post != null && post.getPlace() != null ? post.getPlace().getName() : null;
    }

    @Override
    public long getDueDate() {
        return post != null ? post.getDueDateTime() : 0L;
    }

    @Override
    public void addUploadImageUri(Uri data) {
        uploadUris.add(data);
    }

    @Override
    public List<Uri> getUploadImageUri() {
        return uploadUris;
    }

    @Override
    public void setTitle(String title) {
        if (post != null) {
            post.setTitle(title);
        }
    }

    @Override
    public void setDesc(String desc) {
        if (post != null) {
            post.setDesc(desc);
        }
    }

    @Override
    public void setPlace(Place place) {
        if (post != null) {
            post.setPlace(place);
        }
    }

    @Override
    public void setDueDate(long timeMill) {
        post.setDueDateTime(timeMill);
    }
}
