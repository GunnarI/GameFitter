package com.test.gunnzo.gamefit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gunnar on 16.2.2018.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.main_view_text) TextView mainViewText;

    static final int USER_INFO_REQUEST = 1;
    static final int WELCOME_ACTIVITY_REQUEST_CODE = 11;
    static final String RESULT_DATA_KEY = "has_games_key";

    private static final String USER_DATA_EXTRA = "UserData";

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    public UserData userData = new UserData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
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
            FirebaseUser user = mAuth.getCurrentUser();

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userData = dataSnapshot.getValue(UserData.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read user data", databaseError.toException());
                }
            });

            if (userData.getNrGames() == 0) {
                // TODO: Text the intent function
                Intent intent = new Intent(this, WelcomeActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(USER_DATA_EXTRA, userData);
                intent.putExtras(extras);
                startActivityForResult(intent, WELCOME_ACTIVITY_REQUEST_CODE);
                mainViewText.setText(R.string.no_games);
                Log.i(TAG, "No games");
            } else {
                mainViewText.setText(R.string.some_games);
                Log.i(TAG, "Some games");
            }
        }
    }
}
