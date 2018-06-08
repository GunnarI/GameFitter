package com.test.gunnzo.gamefit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SignupActivityTest {

    @Mock
    SignupActivity activity;


    /*
    @Before
    public void setUp() {
        //activity = new SignupActivity();
        //activity.onCreate(null);
        //activity = Mockito.mock(SignupActivity.class);
    }*/

    @Test
    public void validationMethodShouldReturnTrueOrFalse() throws Exception {
        String name = "TestName";
        String validEmail = "test@test.com";
        String invalidEmail = "test@incorrect";
        String password = "test-this-password";

        //when(activity.validate(name, validEmail, password)).thenReturn(true);
        //when(activity.validate(name, invalidEmail, password)).thenReturn(true);
        assertTrue(activity.validate(name, validEmail, password));
    }

}
