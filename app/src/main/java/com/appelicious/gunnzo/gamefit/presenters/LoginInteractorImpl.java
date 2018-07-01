package com.appelicious.gunnzo.gamefit.presenters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.appelicious.gunnzo.gamefit.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginInteractorImpl implements LoginInteractor {
    private static final String TAG = "LoginInteractor";

    private FirebaseAuth mAuth;
    private Activity executor;

    public LoginInteractorImpl(FirebaseAuth mAuth, Activity executor) {
        this.mAuth = mAuth;
        this.executor = executor;
    }

    @Override
    public void userLogin(String email, String password, final OnLoginFinishedListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(executor, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            listener.onLoginSuccess();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            listener.onLoginFailed();
                        }
                    }
                });
    }
}
