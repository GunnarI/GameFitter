package com.test.gunnzo.gamefit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gunnar on 18.2.2018.
 */

public class UserData implements Parcelable {

    public String username;
    public String email;
    public int nrGames;

    public UserData() {
    }

    public UserData(String username, String email, int nrGames) {
        this.username = username;
        this.email = email;
        this.nrGames = nrGames;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNrGames() {
        return nrGames;
    }

    public void setNrGames(int nrGames) {
        this.nrGames = nrGames;
    }

    public UserData(Parcel in) {
        username = in.readString();
        email = in.readString();
        nrGames = in.readInt();
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
