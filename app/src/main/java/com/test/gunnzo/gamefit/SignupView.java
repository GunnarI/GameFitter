package com.test.gunnzo.gamefit;

public interface SignupView {

    void showProgress();

    void hideProgress();

    void enableButton();

    void disableButton();

    void setUsernameError(String message);

    void setEmailError(String message);

    void setPasswordError(String message);

    void showToast(String message);

    void navigateToHome();
}
