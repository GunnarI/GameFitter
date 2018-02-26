package com.test.gunnzo.gamefit;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_games, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.games_fragment_recycler_view);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GamesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void updateGameRecyclerView(ArrayList<GameData> gamesData) {
        mAdapter.setGameData(gamesData);
    }
}
