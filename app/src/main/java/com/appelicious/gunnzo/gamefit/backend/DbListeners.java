package com.appelicious.gunnzo.gamefit.backend;

import com.appelicious.gunnzo.gamefit.dataclasses.GameData;
import com.appelicious.gunnzo.gamefit.dataclasses.UserData;

public interface DbListeners {
    void onUserDataChanged(UserData userData);
    void onUsernameChanged(String username);
    void onGameAdded(GameData gameData, String gameId);
    void onGameChanged(GameData gameData, String gameId);
    void onGameRemoved(String gameId);
}
