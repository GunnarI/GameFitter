package com.test.gunnzo.gamefit

import android.app.Activity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LoginActivityTest {

    @Test
    fun emailValidator_isCorrect() {
        //val activity: LoginActivity = LoginActivity()

        assertTrue(LoginActivity().isValidEmail("gif@hi.is"))
    }
}
