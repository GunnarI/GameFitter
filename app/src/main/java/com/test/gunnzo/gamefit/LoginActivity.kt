package com.test.gunnzo.gamefit

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider;
//import jdk.nashorn.internal.runtime.ECMAException.getException
import com.google.firebase.auth.FirebaseUser
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential




/**
 * Created by Gunnar on 4.2.2018.
 */
class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private val REQUEST_SIGNUP = 0;

    internal var jsonParser = JSONParser()
    //var progressDialog: ProgressDialog? = null
    private var progressBar: ProgressBar? = null

    // 10.0.2.2 is used instead of localhost to run on emulator
    private val URL_LOGIN_USER = "http://10.0.2.2/gamefitter/login.php"; //"http://192.168.1.82:80/gamefitter/login.php";
    private val TAG_SUCCESS = "success"
    private var success = 0

    private var resultIntent = Intent()
    private val RESULT_DATA_KEY = "has_games_key"

    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val GOOGLE_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                // [START_EXCLUDE]
                // updateUI(null);
                // [END_EXCLUDE]
                onLoginFailed()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        // showProgressDialog()
        // [END_EXCLUDE]
        // TODO: Show progress dialog

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
                        //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        onLoginFailed()
                    }

                    // [START_EXCLUDE]
                    // hideProgressDialog()
                    // [END_EXCLUDE]
                    // TODO: Hide pogress dialog
                }
    }

    fun login_with_google() {
        val googleSignInIntent: Intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN)
    }

    fun login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);

        userLogin(input_email.getText().toString(), input_password.getText().toString())
        //UserLogin().execute()
    }

    fun onLoginSuccess() {
        btn_login.setEnabled(true)
        finish()
    }

    fun onLoginFailed() {
        //Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        btn_login.setEnabled(true)
    }

    fun validate(): Boolean {
        var valid = true

        val email = input_email.getText().toString()
        val password = input_password.getText().toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address")
            valid = false
        } else {
            input_email.setError(null)
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            input_password.setError("between 4 and 10 alphanumeric characters")
            valid = false
        } else {
            input_password.setError(null)
        }

        return valid
    }

    fun userLogin(email:String, password:String) {

        mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        onLoginSuccess()
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        onLoginFailed()
                    }
                }
    }

    /*
    inner class UserLogin : AsyncTask<String, String, String>() {
        val layout: RelativeLayout = RelativeLayout(this@LoginActivity)

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar?.visibility = View.GONE

            if (success == 1) {
                onLoginSuccess()
            } else {
                onLoginFailed()
            }
        }

        override fun doInBackground(vararg p0: String?): String {
            val email = input_email.getText().toString()
            val password = input_password.getText().toString()

            Log.d("Entered email", email)

            val params: HashMap<String, String> = HashMap()
            params.put("email", email)
            params.put("password", password)

            // TODO: implement the database functions and use it instead of setting success to 1
            // TODO:
            /*
            try {
                val json: JSONObject = jsonParser.makeHttpRequest(URL_LOGIN_USER, "GET", params)

                success = json.getInt(TAG_SUCCESS)
            } catch (e: JSONException) {
                e.printStackTrace()
            }*/
            success = 1

            return email
        }

        override fun onPreExecute() {
            super.onPreExecute()
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            progressBar = ProgressBar(this@LoginActivity,null,R.attr.progressBarStyle)
            var params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(100,100)
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            layout.addView(progressBar,params)
            progressBar?.visibility = View.VISIBLE
        }
    }*/
}