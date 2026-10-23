package com.chatapp;

import java.util.Scanner;

/**
 * Entry point - registration, login and then the QuickChat menu.
 */
public class ChatApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Login login = new Login();

    public static void main(String[] args) {
        System.out.println("*** Create an account ***");
        String name = ask("Name: ");
        String surname = ask("Surname: ");

        String username = ask("Username: ");
        while (!login.checkUserName(username)) {
            System.out.println(login.usernameMessage(username));
            username = ask("Username: ");
        }
        System.out.println(login.usernameMessage(username));

        String password = ask("Password: ");
        while (!login.checkPasswordComplexity(password)) {
            System.out.println(login.passwordMessage(password));
            password = ask("Password: ");
        }
        System.out.println(login.passwordMessage(password));

        String cell = ask("Cell phone number: ");
        while (!login.checkCellPhoneNumber(cell)) {
            System.out.println(login.cellNumberMessage(cell));
            cell = ask("Cell phone number: ");
        }
        System.out.println(login.cellNumberMessage(cell));

        System.out.println(login.registerUser(name, surname, username, password, cell));

        System.out.println("\n*** Log in ***");
        boolean loggedIn = false;
        int attemptsLeft = 3;
        while (!loggedIn && attemptsLeft > 0) {
            String user = ask("Username: ");
            String pass = ask("Password: ");
            loggedIn = login.loginUser(user, pass);
            System.out.println(login.returnLoginStatus(loggedIn));
            attemptsLeft--;
        }

        // only logged in users can send messages
        if (loggedIn) {
            runQuickChat();
        } else {
            System.out.println("No attempts left. Exiting.");
        }
    }

    private static void runQuickChat() {
        System.out.println("\nWelcome to QuickChat.");
        int limit = askNumber("How many messages would you like to send? ");
        while (limit <= 0) {
            limit = askNumber("Number must be more than 0: ");
        }

        int messageCount = 0;
        boolean quit = false;

        while (!quit) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");

            switch (askNumber("> ")) {
                case 1:
                    if (messageCount >= limit) {
                        System.out.println("You can only send " + limit + " messages.");
                        break;
                    }
                    for (int i = messageCount; i < limit; i++) {
                        System.out.println("\nEntering message " + (i + 1) + "/" + limit);
                        handleMessage(i);
                        messageCount++;
                    }
                    System.out.println("\nTotal number of messages sent: " + Message.returnTotalMessages());
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    quit = true;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Please enter 1, 2 or 3.");
            }
        }
    }

    private static void handleMessage(int messageNum) {
        String recipient = ask("Recipient cell number: ");
        while (!login.checkCellPhoneNumber(recipient)) {
            System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
            recipient = ask("Recipient cell number: ");
        }

        String content = ask("Type your message: ");
        while (content.isBlank() || content.length() > 250) {
            System.out.println("Please enter a message of less than 250 characters.");
            content = ask("Type your message: ");
        }

        Message message = new Message(messageNum, recipient, content);
        System.out.println(message.checkRecipientCell());
        System.out.println(message.checkMessageLength());

        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int selection = askNumber("> ");
        while (selection < 1 || selection > 3) {
            selection = askNumber("Invalid, choose 1, 2 or 3: ");
        }

        System.out.println(message.sentMessage(selection));

        switch (selection) {
            case 1:
                System.out.println(message.getMessageDetails());
                break;
            case 2:
                while (askNumber("") != 0) {
                    System.out.println("Press 0 to delete the message.");
                }
                System.out.println("Message deleted.");
                break;
            default:
                break;
        }
    }

    private static String ask(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    private static int askNumber(String question) {
        while (true) {
            try {
                return Integer.parseInt(ask(question).trim());
            } catch (NumberFormatException e) {
                System.out.println("Numbers only please.");
            }
        }
    }
}
