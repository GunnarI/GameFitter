package com.appelicious.gunnzo.gamefit.presenters;

import com.appelicious.gunnzo.gamefit.views.GamesView;

public class GamesPresenter {

    private GamesView gamesView;

    public GamesPresenter(GamesView gamesView) {
        this.gamesView = gamesView;
    }

    public void onFabToggle(boolean fabOpen) {
        if (!fabOpen) {
            gamesView.showFabMenu();
        } else {
            gamesView.closeFabMenu();
        }
    }
}
