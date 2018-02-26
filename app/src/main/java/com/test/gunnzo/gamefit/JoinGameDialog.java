package com.test.gunnzo.gamefit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Gunnar on 26.2.2018.
 */

public class JoinGameDialog extends DialogFragment {

    private OnJoinGame mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dark_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_join_game, container, false);

        final EditText joinGameId = mView.findViewById(R.id.join_game_id);
        Button joinButton = mView.findViewById(R.id.join_game_finish);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.joinGame(joinGameId.getText().toString());
                dismiss();
            }
        });

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnJoinGame) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnJoinGame");
        }
    }

    public interface OnJoinGame {
        void joinGame(String gameId);
    }
}
