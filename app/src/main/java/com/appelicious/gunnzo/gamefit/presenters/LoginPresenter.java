package com.appelicious.gunnzo.gamefit.presenters;

import com.appelicious.gunnzo.gamefit.InputValidations;
import com.appelicious.gunnzo.gamefit.views.LoginView;

public class LoginPresenter implements LoginInteractor.OnLoginFinishedListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenter(LoginView loginView, LoginInteractor loginInteractor) {
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
    }

    @Override
    public void onLoginSuccess() {
        loginView.hideProgress();
        loginView.enableButton();
        loginView.navigateToHome();
    }

    @Override
    public void onLoginFailed() {
        loginView.hideProgress();
        loginView.showToast("Authentication failed.");
        loginView.enableButton();
    }

    public void onLoginWithEmail(String email, String password) {
        loginView.disableButton();
        loginView.showProgress();
        loginInteractor.userLogin(email, password, this);
    }

    public void onLoginWithGoogle() {
        // TODO: Implement Google login functionality
    }

    public void onLoginAfterSignup() {
        loginView.navigateToHomeAfterSignup();
    }

    public boolean validate(String email, String password) {
        boolean valid = true;

        InputValidations v = new InputValidations();

        if (!v.isValidEmail(email)) {
            loginView.setEmailError("enter a valid email address");
            valid = false;
        } else {
            loginView.setEmailError(null);
        }

        if (!v.isValidPassword(password)) {
            loginView.setPasswordError("enter a password, at least 6 characters");
            valid = false;
        } else {
            loginView.setPasswordError(null);
        }

        return valid;
    }
}
