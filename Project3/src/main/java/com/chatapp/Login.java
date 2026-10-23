package com.chatapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Registration and login logic for the chat app.
 */
public class Login {

    private String name;
    private String surname;
    private String savedUsername;
    private String savedPassword;
    private String savedCellNumber;

    /**
     * True if the username has an underscore and is not longer than 5 characters.
     */
    public boolean checkUserName(String username) {
        if (username.length() > 5) {
            return false;
        }
        return username.contains("_");
    }

    /**
     * Password rules: 8 or more characters, a capital letter, a number and a special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) {
            return false;
        }

        int capitals = 0, numbers = 0, specials = 0;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                capitals++;
            }
            if (Character.isDigit(ch)) {
                numbers++;
            }
            if (!Character.isLetterOrDigit(ch)) {
                specials++;
            }
        }
        return capitals > 0 && numbers > 0 && specials > 0;
    }

    /**
     * Checks the number starts with +27 and has 9 digits after it.
     *
     * Regex code adapted from: Baeldung. 2024. Validate Phone Numbers With Java Regex.
     * [Online] Available at: https://www.baeldung.com/java-regex-validate-phone-numbers
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        Pattern pattern = Pattern.compile("^\\+27[0-9]{9}$");
        Matcher matcher = pattern.matcher(cellNumber);
        return matcher.matches();
    }

    public String usernameMessage(String username) {
        return checkUserName(username)
                ? "Username successfully captured."
                : "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    public String passwordMessage(String password) {
        return checkPasswordComplexity(password)
                ? "Password successfully captured."
                : "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    public String cellNumberMessage(String cellNumber) {
        return checkCellPhoneNumber(cellNumber)
                ? "Cell number successfully captured."
                : "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    }

    /**
     * Returns the first problem found, otherwise saves the user.
     */
    public String registerUser(String name, String surname, String username, String password, String cellNumber) {
        if (!checkUserName(username)) {
            return usernameMessage(username);
        }
        if (!checkPasswordComplexity(password)) {
            return passwordMessage(password);
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return cellNumberMessage(cellNumber);
        }

        this.name = name;
        this.surname = surname;
        savedUsername = username;
        savedPassword = password;
        savedCellNumber = cellNumber;
        return "User has been registered successfully.";
    }

    public boolean loginUser(String username, String password) {
        boolean usernameMatches = username.equals(savedUsername);
        boolean passwordMatches = password.equals(savedPassword);
        return usernameMatches && passwordMatches;
    }

    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + name + ", " + surname + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getSavedCellNumber() {
        return savedCellNumber;
    }
}
