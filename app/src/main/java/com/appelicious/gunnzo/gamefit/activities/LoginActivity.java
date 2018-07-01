package com.appelicious.gunnzo.gamefit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.appelicious.gunnzo.gamefit.presenters.LoginInteractorImpl;
import com.appelicious.gunnzo.gamefit.presenters.LoginPresenter;
import com.appelicious.gunnzo.gamefit.views.LoginView;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements LoginView {
    //private static final String TAG = "LoginActivity";

    @BindView(R.id.btn_google_login) SignInButton btnGoogleLogin;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.input_email) EditText inputEmail;
    @BindView(R.id.input_password) EditText inputPassword;
    @BindView(R.id.link_signup) TextView linkSignup;
    @BindView(R.id.pb_login) ProgressBar progressBar;

    private int requestSignupKey = 0;

    private Intent resultIntent;
    //private final String resultDataKey = "has_games_key";

    //private GoogleSignInClient mGoogleSignInClient;
    //private final int GOOGLE_SIGN_IN = 9001;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // TODO: Implement the google Sign In functionality
        /*
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGoogle();
            }
        });

        final GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id))
                    .requestEmail()
                    .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(inputEmail.getText().toString(), inputPassword.getText().toString());
            }
        });

        resultIntent = new Intent(this, MainActivity.class);

        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, requestSignupKey);
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        presenter = new LoginPresenter(this, new LoginInteractorImpl(mAuth, this));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestSignupKey) {
            if (resultCode == Activity.RESULT_OK) {
                /*
                resultIntent.putExtra(RESULT_DATA_KEY, false);
                setResult(Activity.RESULT_OK, resultIntent);
                this.finish();
                */
                presenter.onLoginAfterSignup();
            }
        } /*else if (resultCode == GOOGLE_SIGN_IN) {
            //Log.d(TAG, "Here I am")
            GoogleSignIn task = new GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                onLoginFailed()
            }
        }*/
        // TODO: Implement the google login function
    }

    private void login(String email, String password) {
        if (!presenter.validate(email, password)) {
            presenter.onLoginFailed();
            return;
        }

        presenter.onLoginWithEmail(email, password);
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
        btnLogin.setEnabled(true);
    }

    @Override
    public void disableButton() {
        btnLogin.setEnabled(false);
    }

    @Override
    public void setEmailError(String message) {
        inputEmail.setError(message);
    }

    @Override
    public void setPasswordError(String message) {
        inputPassword.setError(message);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHomeAfterSignup() {
        // Is the resultDataKey necessary?:
        //resultIntent.putExtra(resultDataKey, false);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void navigateToHome() {
        finish();
    }
}
