package com.test.gunnzo.gamefit;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Gunnar on 24.2.2018.
 */

public class NewGameFragment extends Fragment {
    private static final String TAG = NewGameFragment.class.getSimpleName();

    public final String USER_DATA_EXTRA = "UserData";
    public final String USER_DATA_UPDATE = "NewGameUserDataUpdate";

    private OnNewGameRequest mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_addgame, container, false);

        Button createGameButton = mView.findViewById(R.id.create_game_button);
        Button joinGameButton = mView.findViewById(R.id.join_game_button);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onOptionSelected(MainActivity.CREATE_NEW_GAME_ID);
            }
        });

        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onOptionSelected(MainActivity.JOIN_NEW_GAME_ID);
            }
        });

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnNewGameRequest) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNewGameRequest");
        }
    }
}
