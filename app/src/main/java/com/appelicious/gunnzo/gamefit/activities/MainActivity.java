package com.appelicious.gunnzo.gamefit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.appelicious.gunnzo.gamefit.CreateGameDialog;
import com.appelicious.gunnzo.gamefit.GameDataList;
import com.appelicious.gunnzo.gamefit.JoinGameDialog;
import com.appelicious.gunnzo.gamefit.OnNewGameRequest;
import com.appelicious.gunnzo.gamefit.R;
import com.appelicious.gunnzo.gamefit.adapters.GamesAdapter;
import com.appelicious.gunnzo.gamefit.dataclasses.GameData;
import com.appelicious.gunnzo.gamefit.dataclasses.UserData;
import com.appelicious.gunnzo.gamefit.fragments.GamesFragment;
import com.appelicious.gunnzo.gamefit.fragments.NewGameFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to identify how a user wants to create a new game.
 * @author Gunnar
 * @version %G% 16.2.2018.
 */

public class MainActivity extends AppCompatActivity
        implements OnNewGameRequest, CreateGameDialog.OnCreateGame, JoinGameDialog.OnJoinGame,
            GamesAdapter.OnLeaveGame {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_fragment_container) View fragmentContainer;

    static final int USER_INFO_REQUEST = 1;
    static final String DIALOG_FRAG_TAG = "dialog";

    public static final int CREATE_NEW_GAME_ID = 101;
    public static final int JOIN_NEW_GAME_ID = 102;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    //private GamesFragment mGamesFragment;
    private OnGamesUpdate updateGameCallback;

    public UserData mUserData;
    public GameData gameData;
    public ArrayList<GameData> mGamesData = new ArrayList<>();

    public ArrayList<GameDataList> mGameDataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // If no fragment has been committed to the activity's ViewGroup then the NewGameFragment is
        // committed.
        if (fragmentContainer != null) {
            if (savedInstanceState == null) {
                NewGameFragment newGameFragment = new NewGameFragment();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, newGameFragment).commit();
            }
        }

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        initiateUi(currentUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Initiates the user-interface depending on if the user is logged in or not. If not logged in
     * then LoginActivity is initiated, otherwise the users profile information is fetched from
     * database to initiate welcome screen.
     *
     * <p>The method also adds listeners on the database so that if profile info changes in the
     * database then the {@link UserData} instance is updated as well.</p>
     * @param currentUser a FirebaseUser class instance containing helper methods to change or
     *                    retrieve profile information
     * @see <a href="https://developers.google.com/android/reference/com/google/firebase/auth/FirebaseUser">FirebaseUser</a>
     * @see #updateUserData(UserData)
     * @see #updateUserName(String)
     * @see #runGamesFragment()
     * @see #updateGamesFragment()
     */
    public void initiateUi(final FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, USER_INFO_REQUEST);
        } else {

            dbRef.child("users").child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            UserData userData = dataSnapshot.getValue(UserData.class);
                            updateUserData(userData);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "Failed to read user data", databaseError.toException());
                        }
                    });

            dbRef.child("users").child(currentUser.getUid()).child("username")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // TODO: Handle cases if username is changed
                            if (mUserData != null) {
                                updateUserName((String)dataSnapshot.getValue());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "Failed to read username", databaseError.toException());
                        }
                    });

            dbRef.child("users").child(currentUser.getUid()).child("gamesIds")
                    .addChildEventListener(new ChildEventListener() {
                // TODO: Handle cases if a game assigned to user is added/changed/removed
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                            if (s == null) {
                                runGamesFragment();
                            }
                            DatabaseReference dbGamesRef =
                                    dbRef.child("games").child(dataSnapshot.getKey());

                            dbGamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    addToGamesData(dataSnapshot.getValue(GameData.class),
                                            dataSnapshot.getKey());

                                    updateGamesFragment();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.w(TAG, "Failed to read games data",
                                            databaseError.toException());
                                }
                            });
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                            String gameId = dataSnapshot.getKey();

                            removeFromGamesData(gameId);

                            updateGamesFragment();
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "Failed to read games id's", databaseError.toException());
                        }
                    });
        }
    }

    /**
     * Updates the username param for the {@link UserData} instance.
     * @param username a string with the username of the user logged in
     */
    private void updateUserName(String username) {
        mUserData.setUsername(username);
    }

    /**
     * Updates the {@link UserData} instance with new instance/updated data, typically retrieved
     * from database.
     * @param userData contains the new instance of {@link UserData}
     */
    private void updateUserData(UserData userData) {
        mUserData = userData;
    }

    private void runNewGameFragment() {
        NewGameFragment newGameFragment = new NewGameFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, newGameFragment).commit();
    }

    /**
     * Adds an new instance of {@link GameData} to the arraylist {@code mGameData}.
     * @param gameData an instance of {@link GameData}
     */
    private void addToGamesData(GameData gameData, String gameId) {
        mGameDataList.add(new GameDataList(gameData.getGameName(), gameData.getGameType(), gameId));
        mGamesData.add(gameData);
    }

    private void removeFromGamesData(String gameId) {
        mUserData.getGamesIds().remove(gameId);
        for (int i = 0; i < mGameDataList.size(); i++) {
            if (mGameDataList.get(i).getGameId().equals(gameId)) {
                mGameDataList.remove(i);
                mGamesData.remove(i);
                return;
            }
        }
    }

    private void runGamesFragment() {
        GamesFragment mGamesFragment = new GamesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, mGamesFragment);
        transaction.commit();

        updateGameCallback = mGamesFragment;
    }

    /**
     * Uses {@code updateGameRecyclerView} method from {@link OnGamesUpdate} interface to update
     * {@link GamesFragment} with new instance of {@link GameData}.
     */
    private void updateGamesFragment() {
        if (mGameDataList.size() > 0) {
            updateGameCallback.updateGameRecyclerView(mGameDataList);
        }
    }

    public interface OnGamesUpdate {
        void updateGameRecyclerView(ArrayList<GameDataList> gamesData);
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
    public void createGame(GameData gameData) {

        try {
            DatabaseReference gamesRef = dbRef.child("games");

            String gameId = gamesRef.push().getKey();
            Map<String, Object> userId = new HashMap<>();
            userId.put(mAuth.getUid(), true);
            gameData.setUserIds(userId);

            DatabaseReference usersRef = dbRef.child("users");

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
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(gameId).exists()) {
                        DatabaseReference usersRef = dbRef.child("users");

                        Map<String, Object> gamesIdsUpdate = new HashMap<>();
                        gamesIdsUpdate.put(dataSnapshot.getKey(), true);
                        usersRef.child(mAuth.getUid())
                                .child("gamesIds").updateChildren(gamesIdsUpdate);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (DatabaseException e) {
            Log.w(TAG, e);
        }
    }

    @Override
    public void leaveGame(String gameId) {
        try {
            DatabaseReference gamesRef = dbRef.child("games");
            DatabaseReference usersRef = dbRef.child("users");

            //String gameId = (String) mUserData.getGamesIds().get(gamePos);
            //Log.i(TAG, gameId);

            usersRef.child(mAuth.getUid())
                    .child("gamesIds")
                    .child(gameId)
                    .removeValue();
            gamesRef.child(gameId).child("userIds").child(mAuth.getUid()).removeValue();


        } catch (DatabaseException e) {
            Log.w(TAG, e);
        }
    }
}
