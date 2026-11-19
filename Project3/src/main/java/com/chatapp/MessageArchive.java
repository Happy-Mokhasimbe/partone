package com.chatapp;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

/**
 * Part 3 - archive of every message captured while the app runs.
 *
 * The messages are kept in parallel arrays: position i in messageIds,
 * hashArr, recipientArr, contentArr and flagArr all belong to the same message.
 */
public class MessageArchive {

    public static final String SENT = "Sent";
    public static final String STORED = "Stored";
    public static final String DISREGARD = "Disregard";

    private final String sender;

    private String[] messageIds;
    private String[] hashArr;
    private String[] recipientArr;
    private String[] contentArr;
    private String[] flagArr;
    private int total;

    /** Messages loaded back in from the JSON file. */
    private Message[] storedFromFile = new Message[0];

    public MessageArchive(String sender, int initialCapacity) {
        this.sender = sender;
        int capacity = initialCapacity > 0 ? initialCapacity : 1;
        messageIds = new String[capacity];
        hashArr = new String[capacity];
        recipientArr = new String[capacity];
        contentArr = new String[capacity];
        flagArr = new String[capacity];
        total = 0;
    }

    /**
     * Records a message in all of the parallel arrays.
     */
    public void record(Message message, String flag) {
        ensureSpace();
        messageIds[total] = message.getMessageId();
        hashArr[total] = message.getHash();
        recipientArr[total] = message.getRecipient();
        contentArr[total] = message.getContent();
        flagArr[total] = flag;
        total++;
    }

    /**
     * Maps the selection used by Message.sentMessage() (1 send, 2 disregard, 3 store) to a flag.
     */
    public static String toFlag(int selection) {
        return selection == 1 ? SENT : selection == 3 ? STORED : DISREGARD;
    }

    private void ensureSpace() {
        if (total < messageIds.length) {
            return;
        }
        int newLength = messageIds.length * 2;
        messageIds = Arrays.copyOf(messageIds, newLength);
        hashArr = Arrays.copyOf(hashArr, newLength);
        recipientArr = Arrays.copyOf(recipientArr, newLength);
        contentArr = Arrays.copyOf(contentArr, newLength);
        flagArr = Arrays.copyOf(flagArr, newLength);
    }

    /**
     * Sent and stored messages are kept, disregarded ones are ignored.
     */
    private boolean isActive(int index) {
        return !DISREGARD.equals(flagArr[index]);
    }

    private String[] contentFlagged(String flag) {
        String[] temp = new String[total];
        int found = 0;
        for (int i = 0; i < total; i++) {
            if (flag.equals(flagArr[i])) {
                temp[found++] = contentArr[i];
            }
        }
        return Arrays.copyOf(temp, found);
    }

    public String[] sentMessages() {
        return contentFlagged(SENT);
    }

    public String[] disregardedMessages() {
        return contentFlagged(DISREGARD);
    }

    public String[] messageHashes() {
        return Arrays.copyOf(hashArr, total);
    }

    public String[] messageIds() {
        return Arrays.copyOf(messageIds, total);
    }

    /**
     * Reads the stored messages JSON file into an array.
     *
     * Code adapted from: Google. 2023. Gson User Guide. [Online] Available at:
     * https://github.com/google/gson/blob/main/UserGuide.md#array-examples
     */
    public Message[] readStoredFromJson(String fileName) {
        try (Reader reader = new FileReader(fileName)) {
            Message[] result = new Gson().fromJson(reader, Message[].class);
            storedFromFile = result != null ? result : new Message[0];
        } catch (IOException e) {
            storedFromFile = new Message[0];
        }
        return storedFromFile;
    }

    public String showStoredFromJson() {
        if (storedFromFile.length == 0) {
            return "No stored messages were found in the JSON file.";
        }
        StringBuilder sb = new StringBuilder("Stored messages read from JSON:");
        for (Message message : storedFromFile) {
            sb.append(System.lineSeparator()).append(System.lineSeparator()).append(message.getMessageDetails());
        }
        return sb.toString();
    }

    /**
     * a. Sender and recipient for every sent / stored message.
     */
    public String listSendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < total; i++) {
            if (isActive(i)) {
                if (sb.length() > 0) {
                    sb.append(System.lineSeparator());
                }
                sb.append(String.format("Sender: %s | Recipient: %s", sender, recipientArr[i]));
            }
        }
        return sb.length() > 0 ? sb.toString() : "There are no messages to display.";
    }

    /**
     * b. The longest sent / stored message.
     */
    public String findLongestMessage() {
        String longest = null;
        for (int i = 0; i < total; i++) {
            if (isActive(i) && (longest == null || contentArr[i].length() > longest.length())) {
                longest = contentArr[i];
            }
        }
        return longest != null ? longest : "There are no messages to display.";
    }

    /**
     * c. Finds a message by its ID and returns the recipient and message.
     */
    public String findByMessageId(String id) {
        for (int i = 0; i < total; i++) {
            if (isActive(i) && messageIds[i].equals(id)) {
                return String.format("Recipient: %s%nMessage: %s", recipientArr[i], contentArr[i]);
            }
        }
        return "Message ID " + id + " was not found.";
    }

    /**
     * d. Every sent / stored message for a recipient.
     */
    public String[] findByRecipient(String recipient) {
        String[] temp = new String[total];
        int found = 0;
        for (int i = 0; i < total; i++) {
            if (isActive(i) && recipientArr[i].equals(recipient)) {
                temp[found++] = contentArr[i];
            }
        }
        return Arrays.copyOf(temp, found);
    }

    /**
     * e. Removes the message with the given hash and closes the gap in the arrays.
     */
    public String deleteByHash(String hash) {
        int position = -1;
        for (int i = 0; i < total; i++) {
            if (hashArr[i].equalsIgnoreCase(hash)) {
                position = i;
                break;
            }
        }
        if (position == -1) {
            return "Message hash " + hash + " was not found.";
        }

        String removed = contentArr[position];
        int toMove = total - position - 1;
        System.arraycopy(messageIds, position + 1, messageIds, position, toMove);
        System.arraycopy(hashArr, position + 1, hashArr, position, toMove);
        System.arraycopy(recipientArr, position + 1, recipientArr, position, toMove);
        System.arraycopy(contentArr, position + 1, contentArr, position, toMove);
        System.arraycopy(flagArr, position + 1, flagArr, position, toMove);
        total--;
        messageIds[total] = null;
        hashArr[total] = null;
        recipientArr[total] = null;
        contentArr[total] = null;
        flagArr[total] = null;

        return "Message: \"" + removed + "\" successfully deleted.";
    }

    /**
     * f. Report with the full details of every sent / stored message.
     */
    public String generateReport() {
        StringBuilder sb = new StringBuilder("---------- Message Report ----------");
        int listed = 0;
        for (int i = 0; i < total; i++) {
            if (isActive(i)) {
                sb.append(System.lineSeparator()).append(System.lineSeparator())
                        .append(String.format("Message ID: %s%nMessage Hash: %s%nRecipient: %s%nMessage: %s%nFlag: %s",
                                messageIds[i], hashArr[i], recipientArr[i], contentArr[i], flagArr[i]));
                listed++;
            }
        }
        if (listed == 0) {
            sb.append(System.lineSeparator()).append("There are no messages to report.");
        }
        return sb.toString();
    }
}
