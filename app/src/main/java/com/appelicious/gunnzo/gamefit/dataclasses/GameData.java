package com.appelicious.gunnzo.gamefit.dataclasses;

import java.util.Map;

/**
 * Created by Gunnar on 18.2.2018.
 */

public class GameData {

    //private String gameId;
    private String gameName;
    private String gameType;
    private Map<String, Object> userIds;

    public GameData() {
    }

    public GameData(String gameName, String gameType) {
        this.gameName = gameName;
        this.gameType = gameType;
    }

    /*
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }*/

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Map<String, Object> getUserIds() {
        return userIds;
    }

    public void setUserIds(Map<String, Object> userIds) {
        this.userIds = userIds;
    }
}
