package com.test.gunnzo.gamefit

import android.app.Activity
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
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


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
    private val URL_CREATE_USER = "http://10.0.2.2/gamefitter/login.php"; //"http://192.168.1.82:80/gamefitter/login.php";
    private val TAG_SUCCESS = "success"
    private var success = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener{login();}

        link_signup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                this.finish()
            }
        }
    }

    fun login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);

        UserLogin().execute()
    }

    fun onLoginSuccess() {
        btn_login.setEnabled(true)
        finish()
    }

    fun onLoginFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

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

            try {
                val json: JSONObject = jsonParser.makeHttpRequest(URL_CREATE_USER, "GET", params)

                success = json.getInt(TAG_SUCCESS)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

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
    }
}