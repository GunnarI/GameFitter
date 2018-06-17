package com.test.gunnzo.gamefit;

public class SignupPresenter implements SignupInteractor.OnSignupFinishedListener {

    private SignupView signupView;
    private SignupInteractor signupInteractor;

    public SignupPresenter(SignupView signupView, SignupInteractor signupInteractor) {
        this.signupView = signupView;
        this.signupInteractor = signupInteractor;
    }

    @Override
    public void onSignupSuccess() {
        signupView.enableButton();

        signupView.navigateToHome();
    }

    @Override
    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        signupView.hideProgress();
        signupView.showToast("Authentication failed.");
        signupView.enableButton();
    }

    @Override
    public void finishedCreatingUser() {
        signupView.hideProgress();
    }

    public void onCreatingUser(String name, String email, String password) {
        signupView.disableButton();

        signupView.showProgress();

        signupInteractor.createNewUser(name, email, password, this);
    }

    public boolean validate(String name, String email, String password) {
        boolean valid = true;

        InputValidations v = new InputValidations();

        if (!v.isValidUsername(name)) {
            signupView.setUsernameError("enter a username, up to 16 characters");
            valid = false;
        } else {
            signupView.setUsernameError(null);
        }

        if (!v.isValidEmail(email)) {
            signupView.setEmailError("enter a valid email address");
            valid = false;
        } else {
            signupView.setEmailError(null);
        }

        if (!v.isValidPassword(password)) {
            signupView.setPasswordError("enter a password, at least 6 characters");
            valid = false;
        } else {
            signupView.setPasswordError(null);
        }

        return valid;
    }
}
