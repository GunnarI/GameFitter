package com.appelicious.gunnzo.gamefit.backend;

import com.appelicious.gunnzo.gamefit.dataclasses.GameData;
import com.appelicious.gunnzo.gamefit.dataclasses.UserData;

public interface DbOperationsContract {

    void setUsername(String username);
    void updateUsername(String username);
    void getUsername();

    void updateUserData(UserData userData);

    void joinGame(String gameId);
    void updateGame(GameData gameData);
    void getGames();
    void leaveGame(String gameId);

    void adminCreateGame(GameData gameData);
    void adminDeleteGame(GameData gameData);
}
