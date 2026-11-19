/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.st10546887rhmokhasimbeprog5121part1;

/**
 *
 * @author Student
 */
public class Login {
      String savedUserName;
    String savedPassWord;
    String savedCellPhone;
    String userFirstName;
    String userLastName;

    // Username must contain an underscore and be no longer than 5 characters
    public boolean checkUserName(String userName) {
        boolean underscoreFound = userName.indexOf('_') != -1;
        boolean lengthValid = userName.length() <= 5;
        return underscoreFound && lengthValid;
    }

    // Password needs 8+ characters, one uppercase, one number, one special character
    public boolean checkPasswordComplexity(String passWord) {
        if (passWord.length() < 8) {
            return false;
        }

        boolean upperFound = false;
        boolean numberFound = false;
        boolean specialFound = false;

        for (int index = 0; index < passWord.length(); index++) {
            char currentChar = passWord.charAt(index);

            if (Character.isUpperCase(currentChar)) {
                upperFound = true;
            } else if (Character.isDigit(currentChar)) {
                numberFound = true;
            } else if (!Character.isLetterOrDigit(currentChar)) {
                specialFound = true;
            }
        }

        return upperFound && numberFound && specialFound;
    }

 
    public boolean checkCellPhoneNumber(String cellPhone) {
        return cellPhone.matches("^\\+27[0-9]{9}$");
    }

    // Registers the user or returns the relevant error message
    public String registerUser(String userName, String passWord, String cellPhone,
                               String firstName, String lastName) {

        if (!checkUserName(userName)) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(passWord)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        savedUserName = userName;
        savedPassWord = passWord;
        savedCellPhone = cellPhone;
        userFirstName = firstName;
        userLastName = lastName;

        return "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.";
    }

    // Verifies login details against stored details
    public boolean loginUser(String userName, String passWord) {
        if (savedUserName == null || savedPassWord == null) {
            return false;
        }
        return savedUserName.equals(userName) && savedPassWord.equals(passWord);
    }

    // Returns success or failure message for login
    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + userFirstName + ", " + userLastName
                    + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
