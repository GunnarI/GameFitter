package com.test.gunnzo.gamefit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SignupPresenterTest {

    SignupPresenter presenter;

    @Mock
    SignupView view;

    @Mock
    SignupInteractor interactor;


    @Before
    public void setUp() throws Exception {
        presenter = new SignupPresenter(view, interactor);
    }

    @Test
    public void onSignupSuccess() {
        // Setup

        // Run
        presenter.onSignupSuccess();

        // Assert
        Mockito.verify(view).enableButton();
        Mockito.verify(view).navigateToHome();
    }

    @Test
    public void onSignupFailed() {
        // Setup
        String message = "Authentication failed.";

        // Run
        presenter.onSignupFailed();

        // Assert
        Mockito.verify(view).hideProgress();
        Mockito.verify(view).showToast(message);
        Mockito.verify(view).enableButton();
    }

    @Test
    public void finishedCreatingUser() {
        // Setup

        // Run
        presenter.finishedCreatingUser();

        // Assert
        Mockito.verify(view).hideProgress();
    }

    @Test
    public void onCreatingUser() {
        // Setup
        String name = "username";
        String email = "some@email.com";
        String password = "RandomPassword123";

        // Run
        presenter.onCreatingUser(name, email, password);

        // Assert
        Mockito.verify(view).disableButton();
        Mockito.verify(view).showProgress();
        Mockito.verify(interactor).createNewUser(name, email, password, presenter);
    }

    @Test
    public void validateValid() {
        // Setup
        String name = "username";
        String email = "some@email.com";
        String password = "RandomPassword123";

        // Run
        boolean valid = presenter.validate(name, email, password);

        // Assert
        assertEquals(true, valid);
        Mockito.verify(view).setUsernameError(null);
        Mockito.verify(view).setEmailError(null);
        Mockito.verify(view).setPasswordError(null);
    }

    @Test
    public void validateUsernameInvalid() {
        // Setup
        String name = "1234567891012141618";
        String email = "some@email.com";
        String password = "RandomPassword123";
        String usernameErrorMessage = "enter a username, up to 16 characters";

        // Run
        boolean valid = presenter.validate(name, email, password);

        // Assert
        assertEquals(false, valid);
        Mockito.verify(view).setUsernameError(usernameErrorMessage);
        Mockito.verify(view).setEmailError(null);
        Mockito.verify(view).setPasswordError(null);
    }

    @Test
    public void validateEmailInvalid() {
        // Setup
        String name = "1234567891012141618";
        String email = "some@email";
        String password = "RandomPassword123";
        String usernameErrorMessage = "enter a username, up to 16 characters";
        String emailErrorMessage = "enter a valid email address";

        // Run
        boolean valid = presenter.validate(name, email, password);

        // Assert
        assertEquals(false, valid);
        Mockito.verify(view).setUsernameError(usernameErrorMessage);
        Mockito.verify(view).setEmailError(emailErrorMessage);
        Mockito.verify(view).setPasswordError(null);
    }

    @Test
    public void validatePasswordInvalid() {
        // Setup
        String name = "1234567891012141618";
        String email = "some@email";
        String password = "123";
        String usernameErrorMessage = "enter a username, up to 16 characters";
        String emailErrorMessage = "enter a valid email address";
        String passwordErrorMessage = "enter a password, at least 6 characters";

        // Run
        boolean valid = presenter.validate(name, email, password);

        // Assert
        assertEquals(false, valid);
        Mockito.verify(view).setUsernameError(usernameErrorMessage);
        Mockito.verify(view).setEmailError(emailErrorMessage);
        Mockito.verify(view).setPasswordError(passwordErrorMessage);
    }
}