package com.appelicious.gunnzo.gamefit;

import com.appelicious.gunnzo.gamefit.presenters.LoginInteractor;
import com.appelicious.gunnzo.gamefit.presenters.LoginPresenter;
import com.appelicious.gunnzo.gamefit.views.LoginView;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    LoginPresenter presenter;

    @Mock
    LoginView view;

    @Mock
    LoginInteractor interactor;
}
