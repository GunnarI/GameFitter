package com.test.gunnzo.gamefit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Gunnar on 16.2.2018.
 */

public class MainActivity extends AppCompatActivity {
    static final int USER_INFO_REQUEST = 1;
    static final String RESULT_DATA_KEY = "has_games_key";

    private FirebaseAuth mAuth;
    //private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivityForResult(intent, USER_INFO_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == USER_INFO_REQUEST) {
            if (resultCode == RESULT_OK) {
                boolean hasGames = data.getBooleanExtra(RESULT_DATA_KEY, false);
            }
        }
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, USER_INFO_REQUEST);
        } else {
            // TODO: Go to either welcome screen or games screen depending on if the user has any existing games or not

        }
    }
}
