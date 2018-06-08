package com.test.gunnzo.gamefit;

public interface SignupFrame {

    void signup(String name, String email, String password);
    void onSignupSuccess();
    void onSignupFailed();

    boolean validate(String name, String email, String password);
}
