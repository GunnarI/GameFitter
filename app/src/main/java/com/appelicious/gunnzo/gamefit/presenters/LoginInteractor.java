package com.appelicious.gunnzo.gamefit.presenters;

public interface LoginInteractor {

    interface OnLoginFinishedListener {
        void onLoginSuccess();

        void onLoginFailed();
    }

    void userLogin(String email, String password, OnLoginFinishedListener listener);
}
