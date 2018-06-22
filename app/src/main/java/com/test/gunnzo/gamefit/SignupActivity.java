package com.test.gunnzo.gamefit;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.gunnzo.gamefit.backend.JSONParser;
import com.test.gunnzo.gamefit.dataclasses.UserData;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by Gunnar on 6.2.2018.
 */

public class SignupActivity extends AppCompatActivity implements SignupView {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText nameText;
    @BindView(R.id.input_email) EditText emailText;
    @BindView(R.id.input_password) EditText passwordText;
    @BindView(R.id.btn_signup) Button signupButton;
    @BindView(R.id.link_login) TextView loginLink;
    @BindView(R.id.pb_signup) ProgressBar progressBar;

    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static int success = 0;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private SignupPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(nameText.getText().toString(),
                        emailText.getText().toString(),
                        passwordText.getText().toString());
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        presenter = new SignupPresenter(this,
                new SignupInteractorImpl(mAuth, dbRef, this));
    }

    public void signup(String name, String email, String password) {
        Log.d(TAG, "Signup");

        if (!presenter.validate(name, email, password)) {
            presenter.onSignupFailed();
            return;
        }

        presenter.onCreatingUser(name, email, password);

        //createNewUser(name, email, password);
    }

    /*
    // TODO: Change validation of signup fields
    public boolean validate(String name, String email, String password) {
        boolean valid = true;

        InputValidations v = new InputValidations();

        if (!v.isValidUsername(name)) {
            nameText.setError("enter a username, up to 16 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (!v.isValidEmail(email)) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (!v.isValidPassword(password)) {
            passwordText.setError("enter a password, at least 6 characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }*/
/*
    public void createNewUser(final String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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

                            presenter.onSignupSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            presenter.onSignupFailed();
                        }

                        presenter.finishedCreatingUser();
                    }
                });
    }*/

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void enableButton() {
        signupButton.setEnabled(true);
    }

    @Override
    public void disableButton() {
        signupButton.setEnabled(false);
    }

    @Override
    public void setUsernameError(String message) {
        nameText.setError(message);
    }

    @Override
    public void setEmailError(String message) {
        emailText.setError(message);
    }

    @Override
    public void setPasswordError(String message) {
        passwordText.setError(message);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(SignupActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        setResult(RESULT_OK, null);
        finish();
    }
}
