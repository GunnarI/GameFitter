package com.test.gunnzo.gamefit;

interface SignupInteractor {

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
