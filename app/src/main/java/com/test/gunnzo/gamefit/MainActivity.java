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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.gunnzo.gamefit.dataclasses.GameData;
import com.test.gunnzo.gamefit.dataclasses.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gunnar on 16.2.2018.
 */

public class MainActivity extends FragmentActivity
        implements OnNewGameRequest, CreateGameDialog.OnCreateGame {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_fragment_container) View fragmentContainer;

    static final int USER_INFO_REQUEST = 1;
    static final String DIALOG_FRAG_TAG = "dialog";

    static final int CREATE_NEW_GAME_ID = 101;
    static final int JOIN_NEW_GAME_ID = 102;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private OnGamesUpdate updateGameCallback;

    public UserData userData = new UserData();
    public ArrayList<GameData> mGamesdata = null;

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
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        initiateUI(currentUser);
    }

    public void initiateUI(final FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, USER_INFO_REQUEST);
        } else {
            dbRef.child("users").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserData userData = dataSnapshot.getValue(UserData.class);

                    if (userData.getGamesIds() != null) {
                        // TODO: Start NewGameFragment with the data that it needs
                        //Log.i(TAG, "User is not part of any games");
                        final ArrayList<GameData> tempGamesData = null;
                        for (DataSnapshot gameId : dataSnapshot.child("gamesIds").getChildren()) {
                            Log.i(TAG,gameId.getKey());
                            DatabaseReference dbGamesRef = dbRef.child("games").child(gameId.getKey());

                            dbGamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i(TAG, "Hér er ég");
                                    tempGamesData.add(dataSnapshot.getValue(GameData.class));
                                    // TODO: Make the getting every relevant game work
                                    /*
                                    if (gameData != null) {
                                        mGamesdata.add(gameData);
                                    }*/
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Failed to read games data", databaseError.toException());
                                }
                            });
                        }
                        if (mGamesdata != null) {
                            updateGamesFragment(userData, tempGamesData);
                        }
                    } /*else {
                        // TODO: Start GamesFragment with the data that it needs


                        //Log.i(TAG, "Some games");
                    }*/
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read user data", databaseError.toException());
                }
            });

            dbRef.child("users").child(currentUser.getUid()).child("username")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // TODO: Handle cases if username is changed
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read user data", databaseError.toException());
                }
            });

            dbRef.child("users").child(currentUser.getUid()).child("gamesIds")
                    .addChildEventListener(new ChildEventListener() {
                // TODO: Handle cases if a game assigned to user is added/changed/removed
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateGamesFragment(UserData ud, ArrayList<GameData> gamesData) {
        this.userData = ud;
        //Log.i(TAG, userData.getEmail());

        GamesFragment gamesFragment = new GamesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, gamesFragment);
        transaction.commit();

        updateGameCallback.updateGameRecyclerView(mGamesdata);
    }

    public interface OnGamesUpdate {
        void updateGameRecyclerView(ArrayList<GameData> gamesData);
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

    @Override
    public void CreateGame(GameData gameData) {

        try {
            DatabaseReference gamesRef = dbRef.child("games");
            DatabaseReference usersRef = dbRef.child("users");

            String gameId = gamesRef.push().getKey();
            //gameData.setGameId(gameId);
            Map<String, Object> userId = new HashMap<>();
            userId.put(mAuth.getUid(), true);
            gameData.setUserIds(userId);

            Map<String, Object> gamesIdsUpdate = new HashMap<>();
            gamesIdsUpdate.put(gameId, true);
            usersRef.child(mAuth.getUid()).child("gamesIds").updateChildren(gamesIdsUpdate);

            gamesRef.child(gameId).setValue(gameData);
        } catch (DatabaseException e) {
            Log.w(TAG, e);
        }

    }
}
