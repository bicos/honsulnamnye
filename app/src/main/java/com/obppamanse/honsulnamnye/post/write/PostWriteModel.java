package com.obppamanse.honsulnamnye.post.write;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
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

import static com.obppamanse.honsulnamnye.firebase.FirebaseUtils.POST_FILENAMES_REF;
import static com.obppamanse.honsulnamnye.firebase.FirebaseUtils.POST_HASH_TAG_REF;
import static com.obppamanse.honsulnamnye.firebase.FirebaseUtils.TIMESTAMP_REF;

/**
 * Created by raehyeong.park on 2017. 5. 25..
 */

public class PostWriteModel implements PostContract.WriteModel {

    private DatabaseReference postRef;

    private Post post;

    private List<Uri> uploadUris;

    private String tmpHashTag;

    public PostWriteModel() {
        postRef = FirebaseUtils.getPostRef();
        post = new Post();
        uploadUris = new ArrayList<>();
    }

    @Override
    public void writePost(final Activity activity, final OnSuccessListener<Void> successListener, final OnFailureListener failureListener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            failureListener.onFailure(new PostContract.NotExistAuthUserException());
            return;
        }

        if (post == null) {
            failureListener.onFailure(new PostContract.FailureWritePostException());
            return;
        }

        if (TextUtils.isEmpty(post.getTitle())) {
            failureListener.onFailure(new PostContract.EmptyTitlePostException());
            return;
        }

        if (TextUtils.isEmpty(post.getDesc())) {
            failureListener.onFailure(new PostContract.EmptyDescPostException());
            return;
        }

        post.setUid(user.getUid());

        final String key = postRef.push().getKey();
        if (TextUtils.isEmpty(key)) {
            failureListener.onFailure(new PostContract.FailureWritePostException());
            return;
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
                final String fileName = key + "_image_" + System.currentTimeMillis();
                FirebaseUtils.getPostStorageRef(key).child(fileName).putFile(uri)
                        .addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                post.addUploadFileName(fileName);
                                e.onNext(taskSnapshot);
                                e.onComplete();
                            }
                        })
                        .addOnFailureListener(activity, new OnFailureListener() {
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

        Task<Void> task = postRef.child(key).setValue(post);

        if (!post.getFileNames().isEmpty()) {
            task.continueWithTask(new Continuation<Void, Task<Void>>() {
                @Override
                public Task<Void> then(@android.support.annotation.NonNull Task<Void> task) throws Exception {
                    if (task.isSuccessful()) {
                        return postRef.child(key).child(POST_FILENAMES_REF).setValue(post.getFileNames());
                    } else {
                        throw task.getException();
                    }
                }
            });
        }

        if (!post.getHashTags().isEmpty()) {
            task.continueWithTask(new Continuation<Void, Task<Void>>() {
                @Override
                public Task<Void> then(@android.support.annotation.NonNull Task<Void> task) throws Exception {
                    return postRef.child(key).child(POST_HASH_TAG_REF).setValue(post.getHashTags());
                }
            });
        }

        task.continueWithTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(@android.support.annotation.NonNull Task<Void> task) throws Exception {
                if (task.isSuccessful()) {
                    return postRef.child(key).child(TIMESTAMP_REF).setValue(ServerValue.TIMESTAMP);
                } else {
                    throw task.getException();
                }
            }
        }).addOnSuccessListener(activity, successListener)
                .addOnFailureListener(activity, failureListener);
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
    public Post getPost() {
        return post;
    }

    @Override
    public List<String> getHashTagList() {
        return post.getHashTags();
    }

    @Override
    public String getHashTag() {
        return tmpHashTag;
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

    @Override
    public void setHashTag(String hashTag) {
        tmpHashTag = hashTag;
    }
}
