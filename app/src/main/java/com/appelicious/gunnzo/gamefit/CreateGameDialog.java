package com.appelicious.gunnzo.gamefit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.appelicious.gunnzo.gamefit.dataclasses.GameData;

/**
 * Created by Gunnar on 24.2.2018.
 */

public class CreateGameDialog extends DialogFragment {
    public static final String TAG = CreateGameDialog.class.getSimpleName();

    public static final String STACK_LEVEL_EXTRA = "stack-level";

    private OnCreateGame mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dark_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_create_game, container, false);

        final EditText newGameName = mView.findViewById(R.id.new_game_name);
        final EditText newGameType = mView.findViewById(R.id.new_game_type);
        Button createButton = mView.findViewById(R.id.create_game_finish);
        Button cancelButton = mView.findViewById(R.id.create_game_cancel);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData gameData = new GameData(
                        newGameName.getText().toString(), newGameType.getText().toString());
                mCallback.createGame(gameData);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnCreateGame) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCreateGame");
        }
    }

    public interface OnCreateGame {
        void createGame(GameData gameData);
    }
}
