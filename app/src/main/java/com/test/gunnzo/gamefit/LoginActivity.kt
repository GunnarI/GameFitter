package com.test.gunnzo.gamefit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.api.ApiException
import java.util.*


/**
 * Created by Gunnar on 4.2.2018.
 */
class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private val REQUEST_SIGNUP = 0;

    private var resultIntent = Intent()
    private val RESULT_DATA_KEY = "has_games_key"

    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val GOOGLE_SIGN_IN = 9001
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = this.pb_login
        mAuth = FirebaseAuth.getInstance()

        // TODO: Fix google login
        btn_google_login.setOnClickListener{login_with_google();};
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_login.setOnClickListener{login();}

        resultIntent = Intent(this, MainActivity::class.java)

        link_signup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "Here I am")
        if(requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                resultIntent.putExtra(RESULT_DATA_KEY, false)
                setResult(Activity.RESULT_OK, resultIntent)
                this.finish()
            }
        } else if (resultCode == GOOGLE_SIGN_IN) {
            //Log.d(TAG, "Here I am")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                onLoginFailed()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        // TODO: Test progressBar ?
        progressBar?.visibility = View.VISIBLE

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth!!.getCurrentUser()
                        onLoginSuccess()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        onLoginFailed()
                    }

                    progressBar?.visibility = View.INVISIBLE
                }
    }

    fun login_with_google() {
        val googleSignInIntent: Intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN)
    }

    fun login() {
        Log.d(TAG, "Login");

        val email = input_email.getText().toString()
        val password = input_password.getText().toString()

        if (!isValidEmail(email) || !isValidPassword(password)) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);

        userLogin(email, password)
    }

    fun onLoginSuccess() {
        btn_login.setEnabled(true)
        finish()
    }

    fun onLoginFailed() {
        btn_login.setEnabled(true)
    }

    fun isValidEmail(email: String): Boolean {
        var emailValid = true

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("Not a valid email address")
            emailValid = false
        } else {
            input_email.setError(null)
        }

        return emailValid
    }

    fun isValidPassword(password: String): Boolean {
        var passwordValid = true

        if (password.isEmpty()) {
            input_password.setError("Please enter the password")
            passwordValid = false
        } else {
            input_password.setError(null)
        }

        return passwordValid
    }

    fun userLogin(email:String, password:String) {
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        onLoginSuccess()
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        input_email.setError("Incorrect email and/or password");
                        input_password.setError("Incorrect email and/or password");
                        onLoginFailed()
                    }
                }
    }
}