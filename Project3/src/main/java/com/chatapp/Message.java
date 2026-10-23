package com.chatapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents one message sent through the chat app.
 */
public class Message {

    // shared between all messages while the app runs
    private static ArrayList<Message> messagesSent = new ArrayList<>();
    private static ArrayList<Message> messagesStored = new ArrayList<>();
    private static int sentCount = 0;

    private String messageId;
    private int messageNum;
    private String recipient;
    private String content;
    private String hash;

    public Message(int messageNum, String recipient, String content) {
        this(randomId(), messageNum, recipient, content);
    }

    public Message(String messageId, int messageNum, String recipient, String content) {
        this.messageId = messageId;
        this.messageNum = messageNum;
        this.recipient = recipient;
        this.content = content;
        this.hash = createMessageHash();
    }

    /**
     * Makes a random ten digit ID.
     */
    private static String randomId() {
        String id = "";
        while (id.length() < 10) {
            id += (int) (Math.random() * 10);
        }
        return id;
    }

    public boolean checkMessageID() {
        return messageId.length() <= 10;
    }

    public String printMessageID() {
        return "Message ID generated: " + messageId;
    }

    /**
     * Uses the same cell number rules as registration.
     */
    public String checkRecipientCell() {
        Login login = new Login();
        return login.checkCellPhoneNumber(recipient)
                ? "Cell phone number successfully captured."
                : "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (content.length() > 250) {
            return "Message exceeds 250 characters by " + (content.length() - 250) + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    /**
     * Hash format: first two digits of ID : message number : first word + last word, all caps.
     */
    public String createMessageHash() {
        String[] words = content.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");

        String result = messageId.substring(0, 2) + ":" + messageNum + ":" + firstWord + lastWord;
        return result.toUpperCase();
    }

    /**
     * 1 - send, 2 - disregard, 3 - store for later.
     */
    public String sentMessage(int selection) {
        String response;
        switch (selection) {
            case 1:
                messagesSent.add(this);
                sentCount++;
                response = "Message successfully sent.";
                break;
            case 2:
                response = "Press 0 to delete the message.";
                break;
            case 3:
                storeMessage();
                response = "Message successfully stored.";
                break;
            default:
                response = "Invalid option.";
        }
        return response;
    }

    public String getMessageDetails() {
        return String.format("Message ID: %s%nMessage Hash: %s%nRecipient: %s%nMessage: %s",
                messageId, hash, recipient, content);
    }

    public static String printMessages() {
        if (messagesSent.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messagesSent.size(); i++) {
            sb.append(messagesSent.get(i).getMessageDetails());
            if (i < messagesSent.size() - 1) {
                sb.append(System.lineSeparator()).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return sentCount;
    }

    /**
     * Stores the message in a JSON file so it can be sent later.
     *
     * Code adapted from: Google. 2023. Gson User Guide. [Online] Available at:
     * https://github.com/google/gson/blob/main/UserGuide.md
     */
    public void storeMessage() {
        messagesStored.add(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(messagesStored);

        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(json);
        } catch (IOException e) {
            System.out.println("Failed to store message: " + e.getMessage());
        }
    }

    public String getMessageId() {
        return messageId;
    }

    public String getHash() {
        return hash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }
}
