package com.appelicious.gunnzo.gamefit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SignupActivityTest {

    //private SignupActivity activity;
    private DatabaseReference mockedDatabaseReference;

    @Before
    public void before() {
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);
        FirebaseDatabase mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);
        //activity = new SignupActivity();
        //activity.onCreate(null);
        //activity = PowerMockito.mock(SignupActivity.class);
        //activity = Robolectric.setupActivity(SignupActivity.class);
    }

    @Test
    public void validationMethodShouldReturnTrueOrFalse() throws Exception {
        // Conditions
        String name = "TestName";
        String validEmail = "test@test.com";
        String invalidEmail = "test@incorrect";
        String password = "test-this-password";
        //Button signupButton = activity.signupButton;

        // Executions
        //activity.nameText.setText(name);
        //activity.emailText.setText(validEmail);
        //activity.passwordText.setText(password);

        //signupButton.performClick();

        // Assertions
        //assertTrue(SignupActivity.validate(name, validEmail, password));
    }

}
