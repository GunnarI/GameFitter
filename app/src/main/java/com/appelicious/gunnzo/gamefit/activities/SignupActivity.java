package com.appelicious.gunnzo.gamefit.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.appelicious.gunnzo.gamefit.R;
import com.appelicious.gunnzo.gamefit.presenters.SignupInteractorImpl;
import com.appelicious.gunnzo.gamefit.presenters.SignupPresenter;
import com.appelicious.gunnzo.gamefit.views.SignupView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Gunnar on 6.2.2018.
 */
public class SignupActivity extends AppCompatActivity implements SignupView {
    //private static final String TAG = "SignupActivity";

    @BindView(R.id.btn_signup) Button signupButton;
    @BindView(R.id.input_name) EditText nameText;
    @BindView(R.id.input_email) EditText emailText;
    @BindView(R.id.input_password) EditText passwordText;
    @BindView(R.id.link_login) TextView loginLink;
    @BindView(R.id.pb_signup) ProgressBar progressBar;

    //JSONParser jsonParser = new JSONParser();
    //private static final String TAG_SUCCESS = "success";
    //private static int success = 0;

    //private FirebaseAuth mAuth;
    //private DatabaseReference dbRef;

    private SignupPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

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


    /**
     * Signup.
     *
     * @param name     the name
     * @param email    the email
     * @param password the password
     */
    public void signup(String name, String email, String password) {
        if (!presenter.validate(name, email, password)) {
            presenter.onSignupFailed();
            return;
        }

        presenter.onCreatingUser(name, email, password);
    }

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
