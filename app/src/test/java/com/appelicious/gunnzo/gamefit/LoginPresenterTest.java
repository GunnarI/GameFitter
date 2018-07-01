package com.appelicious.gunnzo.gamefit;

import com.appelicious.gunnzo.gamefit.presenters.LoginInteractor;
import com.appelicious.gunnzo.gamefit.presenters.LoginPresenter;
import com.appelicious.gunnzo.gamefit.presenters.SignupPresenter;
import com.appelicious.gunnzo.gamefit.views.LoginView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    private LoginPresenter presenter;

    @Mock
    private LoginView view;

    @Mock
    private LoginInteractor interactor;

    @Before
    public void setUp() throws Exception {
        presenter = new LoginPresenter(view, interactor);
    }

    @Test
    public void onLoginSuccess() {
        // Setup

        // Run
        presenter.onLoginSuccess();

        // Assert
        Mockito.verify(view).enableButton();
        Mockito.verify(view).navigateToHome();
    }

    @Test
    public void onLoginFailed() {
        // Setup
        String toastMessage = "Authentication failed.";

        // Run
        presenter.onLoginFailed();

        // Assert
        Mockito.verify(view).hideProgress();
        Mockito.verify(view).showToast(toastMessage);
        Mockito.verify(view).enableButton();
    }

    @Test
    public void onLoginWithEmail() {
        // Setup
        String email = "some@email.com";
        String password = "RandomPassword123";

        // Run
        presenter.onLoginWithEmail(email, password);

        // Assert
        Mockito.verify(view).disableButton();
        Mockito.verify(view).showProgress();
        Mockito.verify(interactor).userLogin(email, password, presenter);
    }

    @Test
    public void onLoginWithGoogle() {
        // TODO: Make a test for Google login functionality
    }

    @Test
    public void onLoginAfterSignup() {
        // Setup

        // Run
        presenter.onLoginAfterSignup();

        //Assert
        Mockito.verify(view).navigateToHomeAfterSignup();
    }

    @Test
    public void validateValid() {
        // Setup
        String email = "some@email.com";
        String password = "RandomPassword123";

        // Run
        boolean valid = presenter.validate(email, password);

        // Assert
        assertEquals(true, valid);
        Mockito.verify(view).setEmailError(null);
        Mockito.verify(view).setPasswordError(null);
    }

    @Test
    public void validateEmailInvalid() {
        // Setup
        String email = "someemail.com";
        String password = "RandomPassword123";
        String emailErrorMessage = "enter a valid email address";

        // Run
        boolean valid = presenter.validate(email, password);

        // Assert
        assertEquals(false, valid);
        Mockito.verify(view).setEmailError(emailErrorMessage);
        Mockito.verify(view).setPasswordError(null);
    }
}
