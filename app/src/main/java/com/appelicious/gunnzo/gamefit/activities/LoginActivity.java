package com.appelicious.gunnzo.gamefit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appelicious.gunnzo.gamefit.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.pb_login) ProgressBar progressBar;
    @BindView(R.id.btn_google_login) SignInButton btnGoogleLogin;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.input_email) EditText inputEmail;
    @BindView(R.id.input_password) EditText inputPassword;
    @BindView(R.id.link_signup) TextView linkSignup;

    private int REQUEST_SIGNUP = 0;

    private Intent resultIntent;
    private final String RESULT_DATA_KEY = "has_games_key";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final int GOOGLE_SIGN_IN = 9001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

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
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        resultIntent = new Intent(this, MainActivity.class);

        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                resultIntent.putExtra(RESULT_DATA_KEY, false);
                setResult(Activity.RESULT_OK, resultIntent);
                this.finish();
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

    private void loginWithGoogle() {
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN);
    }

    private void login() {
        Log.d(TAG, "Login");

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (!isValidEmail(email) || !isValidPassword(password)) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        userLogin(email, password);
    }

    private void onLoginSuccess() {
        btnLogin.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        btnLogin.setEnabled(true);
    }

    private boolean isValidEmail(String email) {
        boolean emailValid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Not a valid email address");
            emailValid = false;
        } else {
            inputEmail.setError(null);
        }

        return emailValid;
    }

    private boolean isValidPassword(String password) {
        boolean passwordValid = true;

        if (password.isEmpty()) {
            inputPassword.setError("Please enter the password");
            passwordValid = false;
        } else {
            inputPassword.setError(null);
        }

        return passwordValid;
    }

    private void userLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            onLoginSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            inputEmail.setError("Incorrect email and/or password");
                            inputPassword.setError("Incorrect email and/or password");

                            onLoginFailed();
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
