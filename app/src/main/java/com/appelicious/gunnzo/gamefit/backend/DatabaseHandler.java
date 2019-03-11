package com.appelicious.gunnzo.gamefit.backend;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appelicious.gunnzo.gamefit.dataclasses.GameData;
import com.appelicious.gunnzo.gamefit.dataclasses.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler implements DbOperationsContract {
    private static final String TAG = DatabaseHandler.class.getSimpleName();

    private static DatabaseHandler INSTANCE = null;

    private DbListeners mCallback;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;

    private DatabaseHandler(Context context) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        mCallback = (DbListeners) context;

        dbRef.child("users").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UserData userData = dataSnapshot.getValue(UserData.class);
                        //updateUserData(userData);
                        mCallback.onUserDataChanged(userData);
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
                        /*
                        if (mUserData != null) {
                            updateUsername((String)dataSnapshot.getValue());
                        }*/
                        mCallback.onUsernameChanged((String)dataSnapshot.getValue());
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
                        DatabaseReference dbGamesRef =
                                dbRef.child("games").child(dataSnapshot.getKey());

                        dbGamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mCallback.onGameAdded(dataSnapshot.getValue(GameData.class),
                                                    dataSnapshot.getKey());
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
                        mCallback.onGameChanged(dataSnapshot.getValue(GameData.class),
                                                dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        mCallback.onGameRemoved(dataSnapshot.getKey());
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

    public static DatabaseHandler getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHandler(context);
        }
        return INSTANCE;
    }

    @Override
    public void setUsername(String username) {

    }

    @Override
    public void updateUsername(String username) {

    }

    @Override
    public void getUsername() {

    }

    @Override
    public void updateUserData(UserData userData) {

    }

    @Override
    public void joinGame(String gameId) {
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
    public void updateGame(GameData gameData) {

    }

    @Override
    public void getGames() {

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

    @Override
    public void adminCreateGame(GameData gameData) {
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
    public void adminDeleteGame(GameData gameData) {

    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }
}


