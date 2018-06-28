package com.appelicious.gunnzo.gamefit.presenters;

public interface SignupInteractor {

    interface OnSignupFinishedListener {
        void onSignupSuccess();
        void onSignupFailed();
        void finishedCreatingUser();
    }

    void createNewUser(String username,
                       String email,
                       String password,
                       OnSignupFinishedListener listener);
}
