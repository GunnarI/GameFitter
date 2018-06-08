package com.test.gunnzo.gamefit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.gunnzo.gamefit.adapters.GamesAdapter;
import com.test.gunnzo.gamefit.dataclasses.GameData;

import java.util.ArrayList;

/**
 * Created by Gunnar on 24.2.2018.
 */

public class GamesFragment extends Fragment
        implements MainActivity.OnGamesUpdate {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GamesAdapter mAdapter;

    private OnNewGameRequest mCallback;

    private FloatingActionButton createGameButton;
    private FloatingActionButton joinGameButton;
    private boolean fabOpen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle("Games");
    }

    @Override
    public void onStart() {
        super.onStart();
        fabOpen = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_games, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.games_fragment_recycler_view);

        FloatingActionButton addGameButton = mView.findViewById(R.id.add_game_float_button);
        createGameButton = mView.findViewById(R.id.create_game_float_button);
        joinGameButton = mView.findViewById(R.id.join_game_float_button);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GamesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fabOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

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

    private void showFABMenu(){
        fabOpen=true;
        createGameButton.animate()
                .translationY(-getResources().getDimension(R.dimen.cg_button_offset));
        joinGameButton.animate()
                .translationY(-getResources().getDimension(R.dimen.jg_button_offset));
    }

    private void closeFABMenu(){
        fabOpen=false;
        createGameButton.animate().translationY(0);
        joinGameButton.animate().translationY(0);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void updateGameRecyclerView(ArrayList<GameDataList> gamesData) {
        mAdapter.setGameData(gamesData);
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
