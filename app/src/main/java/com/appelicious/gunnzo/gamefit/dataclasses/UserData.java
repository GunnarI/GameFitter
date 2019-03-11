package com.appelicious.gunnzo.gamefit.dataclasses;

import java.util.Map;
import java.util.Observable;

/**
 * Class containing the user information retrieved from database for the user logged in.
 *
 * @author Gunnar Ingi Friðriksson
 * @version 1.0, 18.2.2018
 */

public class UserData extends Observable { //implements Parcelable {

    private String username;
    private String email;
    private Map<String, Object> gamesIds;

    public UserData() {
    }

    public UserData(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Gets username.
     *
     * @return a string containing the username of the current user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets username.
     *
     * @param username username to assign to current user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets email.
     *
     * @return a string containing the email of the current user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets games ids.
     *
     * @return a String array with all the game id's of the user
     */
    public Map<String, Object> getGamesIds() {
        return this.gamesIds;
    }

    /**
     * Sets games ids.
     *
     * @param gamesIds String array containing the id's of the games assigned to the user
     */
    public void setGamesIds(Map<String, Object> gamesIds) {
        this.gamesIds = gamesIds;
    }
}
