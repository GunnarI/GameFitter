package com.test.gunnzo.gamefit;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gunnar on 16.2.2018.
 */

public class MainActivity extends AppCompatActivity
        implements OnNewGameRequest, CreateGameDialog.OnCreateGame, JoinGameDialog.OnJoinGame {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_fragment_container) View fragmentContainer;

    static final int USER_INFO_REQUEST = 1;
    static final String DIALOG_FRAG_TAG = "dialog";

    static final int CREATE_NEW_GAME_ID = 101;
    static final int JOIN_NEW_GAME_ID = 102;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private GamesFragment mGamesFragment;
    private OnGamesUpdate updateGameCallback;

    public UserData mUserData;// = new UserData();
    public GameData gameData = new GameData();
    public ArrayList<GameData> mGamesData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (fragmentContainer != null) {
            if (savedInstanceState == null) {
                NewGameFragment newGameFragment = new NewGameFragment();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, newGameFragment).commit();
            }
        }

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        // TODO: Check if these changes work. This was in onStart before but put here to prevent duplication in GamesFragment
        FirebaseUser currentUser = mAuth.getCurrentUser();
        initiateUI(currentUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
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
                    updateUserData(userData);
/*
                    if (userData.getGamesIds() != null) {
                        runGamesFragment();

                        for (DataSnapshot gameId : dataSnapshot.child("gamesIds").getChildren()) {
                            DatabaseReference dbGamesRef =
                                    dbRef.child("games").child(gameId.getKey());

                            dbGamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    addToGamesData(dataSnapshot.getValue(GameData.class));

                                    updateGamesFragment();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w(TAG, "Failed to read games data",
                                            databaseError.toException());
                                }
                            });
                        }
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
                    if (mUserData != null) {
                        updateUserName((String)dataSnapshot.getValue());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read username", databaseError.toException());
                }
            });

            dbRef.child("users").child(currentUser.getUid()).child("gamesIds")
                    .addChildEventListener(new ChildEventListener() {
                // TODO: Handle cases if a game assigned to user is added/changed/removed
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //Log.i(TAG,"Child added: " + s);
                    if (s == null) {
                        Log.i(TAG,"First child");
                        runGamesFragment();
                    }
                    DatabaseReference dbGamesRef =
                            dbRef.child("games").child(dataSnapshot.getKey());

                    dbGamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            addToGamesData(dataSnapshot.getValue(GameData.class));

                            updateGamesFragment();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "Failed to read games data",
                                    databaseError.toException());
                        }
                    });
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
                    Log.w(TAG, "Failed to read games id's", databaseError.toException());
                }
            });
        }
    }

    private void updateUserName(String username) {
        mUserData.setUsername(username);
    }

    private void updateUserData(UserData userData) {
        mUserData = userData;
    }

    private void runNewGameFragment() {
        NewGameFragment newGameFragment = new NewGameFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, newGameFragment).commit();
    }

    private void addToGamesData(GameData gameData) {
        mGamesData.add(gameData);
    }

    private void runGamesFragment() {
        mGamesFragment = new GamesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, mGamesFragment);
        transaction.commit();

        updateGameCallback = mGamesFragment;
    }

    private void updateGamesFragment() {
        if (mGamesData.size() > 0) {
            updateGameCallback.updateGameRecyclerView(mGamesData);
        }
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(DIALOG_FRAG_TAG);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment joinGameFragment = new JoinGameDialog();
            joinGameFragment.show(ft, DIALOG_FRAG_TAG);
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

    @Override
    public void joinGame(final String gameId) {
        // TODO: Let user now if id is incorrect
        try {
            DatabaseReference gamesRef = dbRef.child("games");
            gamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(gameId).exists()) {
                        DatabaseReference usersRef = dbRef.child("users");

                        Map<String, Object> gamesIdsUpdate = new HashMap<>();
                        gamesIdsUpdate.put(dataSnapshot.getKey(), true);
                        usersRef.child(mAuth.getUid())
                                .child("gamesIds").updateChildren(gamesIdsUpdate);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (DatabaseException e) {
            Log.w(TAG, e);
        }
    }
}
