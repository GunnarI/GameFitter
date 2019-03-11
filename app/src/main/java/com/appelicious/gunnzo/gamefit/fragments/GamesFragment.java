package com.appelicious.gunnzo.gamefit.fragments;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appelicious.gunnzo.gamefit.GameDataListItem;
import com.appelicious.gunnzo.gamefit.OnNewGameRequest;
import com.appelicious.gunnzo.gamefit.R;
import com.appelicious.gunnzo.gamefit.activities.MainActivity;
import com.appelicious.gunnzo.gamefit.adapters.GamesAdapter;
import com.appelicious.gunnzo.gamefit.presenters.GamesPresenter;
import com.appelicious.gunnzo.gamefit.views.GamesView;
import java.util.ArrayList;

/**
 * Created by Gunnar on 24.2.2018.
 */

public class GamesFragment extends Fragment
        implements MainActivity.OnGamesUpdate, GamesView {

    @BindView(R.id.games_fragment_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.add_game_float_button) FloatingActionButton addGameButton;
    @BindView(R.id.create_game_float_button) FloatingActionButton createGameButton;
    @BindView(R.id.join_game_float_button) FloatingActionButton joinGameButton;

    private GamesPresenter presenter;
    private GamesAdapter mAdapter;
    private OnNewGameRequest mCallback;
    private boolean fabOpen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkNotNull(((MainActivity) getActivity())).setActionBarTitle("Games");
    }

    @Override
    public void onStart() {
        super.onStart();
        fabOpen = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_games, container, false);
        ButterKnife.bind(this, mView);

        presenter = new GamesPresenter(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GamesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onFabToggle(fabOpen);
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void updateGameRecyclerView(ArrayList<GameDataListItem> gamesData) {
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

    @Override
    public void showFabMenu() {
        fabOpen = true;
        createGameButton.animate()
                .translationY(-getResources().getDimension(R.dimen.cg_button_offset));
        joinGameButton.animate()
                .translationY(-getResources().getDimension(R.dimen.jg_button_offset));
    }

    @Override
    public void closeFabMenu() {
        fabOpen = false;
        createGameButton.animate().translationY(0);
        joinGameButton.animate().translationY(0);
    }

    @Override
    public void showProgress() {
        // TODO: Implement progress bar to show when recycler view is being updated
    }

    @Override
    public void hideProgress() {
        // TODO: Implement progress bar to show when recycler view is being updated
    }
}
