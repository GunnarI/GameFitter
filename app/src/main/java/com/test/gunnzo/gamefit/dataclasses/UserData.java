package com.test.gunnzo.gamefit.dataclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class containing the user information retrieved from database for the user logged in
 *
 * @author Gunnar Ingi Friðriksson
 * @version 1.0, 18.2.2018
 */

public class UserData implements Parcelable {

    private String username;
    private String email;
    private int nrGames;

    public UserData() {
    }

    /**
     * @return a string containing the username of the current user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     *
     * @param username username to assign to current user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return a string containing the email of the current user
     */
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {this.email = email;}

    /**
     * @return an integer containing the number of games a user is currently a part of
     */
    public int getNrGames() {
        return this.nrGames;
    }

    /**
     * @param nrGames integer number of games to assign to the current user
     */
    public void setNrGames(int nrGames) {
        this.nrGames = nrGames;
    }

    private UserData(Parcel in) {
        this.username = in.readString();
        this.email = in.readString();
        this.nrGames = in.readInt();
    }

    public UserData(String username, String email, int nrGames) {
        this.username = username;
        this.email = email;
        this.nrGames = nrGames;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeInt(nrGames);
    }

    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>(){
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
}
