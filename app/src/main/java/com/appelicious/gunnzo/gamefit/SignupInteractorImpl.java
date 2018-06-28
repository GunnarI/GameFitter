package com.appelicious.gunnzo.gamefit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appelicious.gunnzo.gamefit.presenters.SignupInteractor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.appelicious.gunnzo.gamefit.dataclasses.UserData;

public class SignupInteractorImpl implements SignupInteractor {
    private static final String TAG = "SignupInteractor";

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private Activity executor;

    public SignupInteractorImpl(FirebaseAuth mAuth, DatabaseReference dbRef, Activity executor) {
        this.mAuth = mAuth;
        this.dbRef = dbRef;
        this.executor = executor;
    }

    @Override
    public void createNewUser(final String username,
                              String email,
                              String password,
                              final OnSignupFinishedListener listener) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(executor, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserData userData = new UserData(
                                        username, user.getEmail());
                                dbRef.child("users").child(user.getUid()).setValue(userData);
                            }

                            listener.onSignupSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            listener.onSignupFailed();
                        }

                        listener.finishedCreatingUser();
                    }
                });
    }
}
