package com.chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginTest {

    private Login login;

    @BeforeEach
    void init() {
        login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    @Test
    void testCorrectUsername() {
        assertEquals("Username successfully captured.", login.usernameMessage("kyl_1"));
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus(login.loginUser("kyl_1", "Ch&&sec@ke99!")));
    }

    @Test
    void testIncorrectUsername() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.usernameMessage("kyle!!!!!!!"));
    }

    @Test
    void testCorrectPassword() {
        assertEquals("Password successfully captured.", login.passwordMessage("Ch&&sec@ke99!"));
    }

    @Test
    void testIncorrectPassword() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.passwordMessage("password"));
    }

    @Test
    void testCorrectCellNumber() {
        assertEquals("Cell number successfully captured.", login.cellNumberMessage("+27838968976"));
    }

    @Test
    void testIncorrectCellNumber() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
                login.cellNumberMessage("08966553"));
    }

    @Test
    void testRegisterWithBadPassword() {
        Login other = new Login();
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                other.registerUser("Kyle", "Smith", "kyl_1", "password", "+27838968976"));
    }

    @Test
    void testLoginSuccess() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginFail() {
        assertFalse(login.loginUser("kyle", "Ch&&sec@ke99!"));
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(false));
    }

    @Test
    void testUsernameFormatTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void testUsernameFormatFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void testPasswordComplexityTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void testPasswordComplexityFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void testCellNumberTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void testCellNumberFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}
