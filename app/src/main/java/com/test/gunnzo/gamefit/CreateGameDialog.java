package com.test.gunnzo.gamefit;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;

/**
 * Created by Gunnar on 24.2.2018.
 */

public class CreateGameDialog extends DialogFragment {
    public static final String TAG = CreateGameDialog.class.getSimpleName();

    public static final String STACK_LEVEL_EXTRA = "stack-level";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_Dark_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_create_game, container, false);

        Button createButton = mView.findViewById(R.id.create_game_finish);
        Button cancelButton = mView.findViewById(R.id.create_game_cancel);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implement on create button function
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
}
