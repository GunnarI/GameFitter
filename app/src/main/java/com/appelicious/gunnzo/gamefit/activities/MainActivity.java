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
import com.appelicious.gunnzo.gamefit.GameDataListItem;
import com.appelicious.gunnzo.gamefit.JoinGameDialog;
import com.appelicious.gunnzo.gamefit.OnNewGameRequest;
import com.appelicious.gunnzo.gamefit.R;
import com.appelicious.gunnzo.gamefit.adapters.GamesAdapter;
import com.appelicious.gunnzo.gamefit.backend.DatabaseHandler;
import com.appelicious.gunnzo.gamefit.backend.DbListeners;
import com.appelicious.gunnzo.gamefit.dataclasses.GameData;
import com.appelicious.gunnzo.gamefit.dataclasses.UserData;
import com.appelicious.gunnzo.gamefit.fragments.GamesFragment;
import com.appelicious.gunnzo.gamefit.fragments.NewGameFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
            GamesAdapter.OnLeaveGame, DbListeners {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_fragment_container) View fragmentContainer;

    static final int USER_INFO_REQUEST = 1;
    static final String DIALOG_FRAG_TAG = "dialog";

    public static final int CREATE_NEW_GAME_ID = 101;
    public static final int JOIN_NEW_GAME_ID = 102;

    private OnGamesUpdate updateGameCallback;

    public DatabaseHandler mDbHandler;

    public UserData mUserData;
    public GameData gameData;
    public ArrayList<GameData> mGamesData = new ArrayList<>();

    public ArrayList<GameDataListItem> mGameDataList = new ArrayList<>();

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

        mDbHandler = DatabaseHandler.getInstance(this);
        initiateUi();
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
     * @see <a href="https://developers.google.com/android/reference/com/google/firebase/auth/FirebaseUser">FirebaseUser</a>
     * @see #runGamesFragment()
     * @see #updateGamesFragment()
     */
    public void initiateUi() {
        if (mDbHandler.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, USER_INFO_REQUEST);
        } else {
            runGamesFragment();
        }
    }

    private void runNewGameFragment() {
        NewGameFragment newGameFragment = new NewGameFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, newGameFragment).commit();
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
        void updateGameRecyclerView(ArrayList<GameDataListItem> gamesData);
    }

    @Override
    public void onUserDataChanged(UserData userData) {
        mUserData = userData;
    }

    @Override
    public void onUsernameChanged(String username) {
        if (mUserData != null) {
            mUserData.setUsername(username);
        }
    }

    @Override
    public void onGameAdded(GameData gameData, String gameId) {
        mGameDataList.add(new GameDataListItem(
                                gameData.getGameName(), gameData.getGameType(), gameId));
        mGamesData.add(gameData);

        updateGamesFragment();
    }

    @Override
    public void onGameChanged(GameData gameData, String gameId){
        updateGamesFragment();
    };

    @Override
    public void onGameRemoved(String gameId) {
        mUserData.getGamesIds().remove(gameId);
        for (int i = 0; i < mGameDataList.size(); i++) {
            if (mGameDataList.get(i).getGameId().equals(gameId)) {
                mGameDataList.remove(i);
                mGamesData.remove(i);
                return;
            }
        }

        updateGamesFragment();
    };

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
        mDbHandler.adminCreateGame(gameData);
    }

    @Override
    public void joinGame(final String gameId) {
        mDbHandler.joinGame(gameId);
        // TODO: Let user now if id is incorrect
    }

    @Override
    public void leaveGame(String gameId) {
        mDbHandler.leaveGame(gameId);
    }
}
