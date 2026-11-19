/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.st10546887rhmokhasimbeprog5121part1;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class ST10546887RHMOKHASIMBEPROG5121Part1 {

    public static void main(String[] args) {
     Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=== Registration ===");

        System.out.print("Enter first name: ");
        String name = scanner.nextLine();

        System.out.print("Enter last name: ");
        String surname = scanner.nextLine();

        System.out.print("Enter username: ");
        String userName = scanner.nextLine();

        if (login.checkUserName(userName)) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.");
        }

        System.out.print("Enter password: ");
        String passWord = scanner.nextLine();

        if (login.checkPasswordComplexity(passWord)) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.");
        }

        System.out.print("Enter cell phone number (e.g. +27831234567): ");
        String cellPhone = scanner.nextLine();

        if (login.checkCellPhoneNumber(cellPhone)) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        String registrationResult = login.registerUser(userName, passWord, cellPhone, name, surname);
        System.out.println(registrationResult);

        System.out.println("\n=== Login ===");

        System.out.print("Enter username: ");
        String loginName = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPass = scanner.nextLine();

        boolean success = login.loginUser(loginName, loginPass);
        System.out.println(login.returnLoginStatus(success));
    }
}
