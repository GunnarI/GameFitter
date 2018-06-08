package com.test.gunnzo.gamefit;

import com.test.gunnzo.gamefit.dataclasses.GameData;

import java.util.Map;

public class GameDataList {

    private String gameName;
    private String gameType;
    private String gameId;

    public GameDataList(String gameName, String gameType, String gameId) {
        this.gameName = gameName;
        this.gameType = gameType;
        this.gameId = gameId;
    }

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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
