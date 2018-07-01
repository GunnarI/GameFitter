package com.appelicious.gunnzo.gamefit.views;

public interface LoginView {

    void showProgress();

    void hideProgress();

    void enableButton();

    void disableButton();

    void setEmailError(String message);

    void setPasswordError(String message);

    void showToast(String message);

    void navigateToHomeAfterSignup();

    void navigateToHome();
}
