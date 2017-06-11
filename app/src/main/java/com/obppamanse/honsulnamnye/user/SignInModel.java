package com.obppamanse.honsulnamnye.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.obppamanse.honsulnamnye.R;
import com.obppamanse.honsulnamnye.firebase.FirebaseUtils;

import java.util.Arrays;

/**
 * Created by raehyeong.park on 2017. 4. 26..
 */

public class SignInModel implements SignInContract.Model, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1000;

    private String email;

    private String password;

    private SignInCompleteListener listener;

    // 파이어베이스 로그인 모듈
    private FirebaseAuth auth;

    // 페이스북 로그인 모듈
    private LoginManager facebookLoginManager;
    private CallbackManager manager;

    // 구글 로그인 모듈
    private GoogleApiClient googleApiClient;

    public SignInModel() {
        auth = FirebaseAuth.getInstance();

        this.facebookLoginManager = LoginManager.getInstance();
        this.manager = CallbackManager.Factory.create();
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void signInWithFacebook(final Fragment fragment, final SignInCompleteListener listener) {
        this.listener = listener;
        facebookLoginManager.registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                requestSignIn(fragment, credential);
            }

            @Override
            public void onCancel() {
                if (SignInModel.this.listener != null) {
                    SignInModel.this.listener.onFailed(new SignInContract.SignInCancelException());
                }
            }

            @Override
            public void onError(FacebookException error) {
                if (SignInModel.this.listener != null) {
                    SignInModel.this.listener.onFailed(error);
                }
            }
        });
        facebookLoginManager.logInWithReadPermissions(fragment, Arrays.asList("email", "public_profile"));
    }

    @Override
    public void signInWithGoogle(Fragment fragment, SignInCompleteListener listener) {
        this.listener = listener;

        if (googleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(fragment.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(fragment.getActivity())
                    .enableAutoManage(fragment.getActivity(), this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void signInWithEmail(Fragment fragment, SignInCompleteListener listener) {
        this.listener = listener;

        if (TextUtils.isEmpty(email)) {
            listener.onFailed(new SignInContract.EmailIsEmptyException());
            return;
        }

        if (TextUtils.isEmpty(password)) {
            listener.onFailed(new SignInContract.PasswordIdEmptyException());
            return;
        }

        if (!email.contains("@")) {
            listener.onFailed(new SignInContract.EmailNotValidException());
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        requestSignIn(fragment, credential);
    }

    @Override
    public void onActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        if (manager != null) {
            manager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    requestSignIn(fragment, GoogleAuthProvider.getCredential(account.getIdToken(), null));
                } else {
                    if (listener != null) {
                        listener.onFailed(new SignInContract.SignInFailedException(result.getStatus().getStatusMessage()));
                    }
                }
            } else {
                if (listener != null) {
                    listener.onFailed(new SignInContract.SignInFailedException(result.getStatus().getStatusMessage()));
                }
            }
        }
    }

    private void requestSignIn(Fragment fragment, final AuthCredential authCredential) {
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(fragment.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (listener == null) {
                            return;
                        }

                        if (task.isSuccessful()) {
                            listener.onSuccess();
                        } else {
                            listener.onFailed(task.getException());
                        }
                    }
                });
    }

    @Override
    public void requestSignUp(Fragment fragment, final SignInCompleteListener listener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(fragment.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (listener != null) {
                            if (task.isSuccessful()) {
                                listener.onSuccess();
                            } else {
                                listener.onFailed(task.getException());
                            }
                        }
                    }
                });
    }

    @Override
    public void isUserSignedUp(final SignedUpCompleteListener listener) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUtils.getUserRef()
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listener.onSuccess(dataSnapshot.hasChild(user.getUid()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listener.onFailed(databaseError.toException());
                        }
                    });
        } else {
            listener.onSuccess(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (listener == null) {
            return;
        }

        listener.onFailed(new SignInContract.SignInFailedException(connectionResult.getErrorMessage()));
    }

    public interface SignedUpCompleteListener {
        void onSuccess(boolean isSignedUp);

        void onFailed(Exception e);
    }

    public interface SignInCompleteListener {

        void onSuccess();

        void onFailed(Exception e);
    }
}
