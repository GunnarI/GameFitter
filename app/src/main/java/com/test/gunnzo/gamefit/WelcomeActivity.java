package com.test.gunnzo.gamefit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.test.gunnzo.gamefit.dataclasses.UserData;

import butterknife.BindView;

/**
 * @author Gunnar Ingi Friðriksson
 * @version 1.0, 18.2.2018
 */

public class WelcomeActivity extends AppCompatActivity{
    private static final String TAG = WelcomeActivity.class.getSimpleName();

    @BindView(R.id.welcome_message) TextView welcomeText;

    private static final String USER_DATA_EXTRA = "UserData";

    private UserData userData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addgame);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Bundle extras = intentThatStartedThisActivity.getExtras();

            if (extras.containsKey(USER_DATA_EXTRA)) {
                userData = extras.getParcelable(USER_DATA_EXTRA);
                welcomeText.setText(userData.getUsername());
                //Log.i(TAG, (String)userData.getEmail());
            }
        }
    }
}
