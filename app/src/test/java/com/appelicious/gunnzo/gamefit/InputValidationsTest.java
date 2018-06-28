package com.appelicious.gunnzo.gamefit;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gunnar on 26.2.2018.
 */

public class InputValidationsTest {

    private InputValidations v = new InputValidations();

    @Test
    public void emailValidator_NotValidEmails() {
        assertFalse("Empty \"email\" is valid", v.isValidEmail(""));
        assertFalse("Email missing @ symbol is valid",
                v.isValidEmail("asdfgmail.com"));
        assertFalse("Email missing a dot is valid",
                v.isValidEmail("asdf@gmailcom"));
        assertFalse("Email missing the name is valid",
                v.isValidEmail("@gmail.com"));
        assertFalse("Email missing the domain name is valid",
                v.isValidEmail("asdf@.com"));
        assertFalse("Email missing the top level domain is valid",
                v.isValidEmail("asdf@gmail."));
        assertFalse("Email missing the whole domain is valid",
                v.isValidEmail("asdf@"));
    }

    @Test
    public void emailValidator_ValidEmails() {
        assertTrue(v.isValidEmail("gif@hi.is"));
        assertTrue(v.isValidEmail("g@h.i"));
        assertTrue(v.isValidEmail("12345678@gmail.com"));
    }

    // TODO: Implement username, password, gamename tests
}
