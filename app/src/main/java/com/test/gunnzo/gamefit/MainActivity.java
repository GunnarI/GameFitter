package com.test.gunnzo.gamefit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.gunnzo.gamefit.dataclasses.UserData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gunnar on 16.2.2018.
 */

public class MainActivity extends FragmentActivity implements OnNewGameRequest {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_fragment_container) View fragmentContainer;

    static final int USER_INFO_REQUEST = 1;
    static final String DIALOG_FRAG_TAG = "dialog";

    static final int CREATE_NEW_GAME_ID = 101;
    static final int JOIN_NEW_GAME_ID = 102;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    public UserData userData = new UserData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (fragmentContainer != null) {
            if (savedInstanceState != null) {
                return;
            }

            NewGameFragment newGameFragment = new NewGameFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container, newGameFragment).commit();
        }

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(final FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, USER_INFO_REQUEST);
        } else {
            dbRef.child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userData = dataSnapshot.getValue(UserData.class);

                    if (userData.getNrGames() == 0) {
                        // TODO: Start NewGameFragment with the data that it needs
                    } else {
                        // TODO: Start GamesFragment with the data that it needs
                        GamesFragment gamesFragment = new GamesFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_fragment_container, gamesFragment);
                        transaction.commit();
                        Log.i(TAG, "Some games");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read user data", databaseError.toException());
                }
            });


        }
    }

    @Override
    public void onOptionSelected(int optionId) {
        if (optionId == CREATE_NEW_GAME_ID) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(DIALOG_FRAG_TAG);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment createGameFragment = new CreateGameDialog();
            createGameFragment.show(ft, DIALOG_FRAG_TAG);
        } else if (optionId == JOIN_NEW_GAME_ID) {
            // TODO: Start join new game dialog
        }
    }
}
