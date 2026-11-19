package com.chatapp;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageArchiveTest {

    private MessageArchive archive;
    private Message[] testMessages;

    /**
     * Populates the arrays with the Part 3 test data (messages 1 - 5).
     */
    @BeforeEach
    void populate() {
        archive = new MessageArchive("+27838968976", 5);
        testMessages = new Message[]{
            new Message(0, "+27834557896", "Did you get the cake?"),
            new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time."),
            new Message(2, "+27834484567", "Yohoooo, I am at your gate."),
            new Message(3, "0838884567", "It is dinner time!"),
            new Message(4, "+27838884567", "Ok, I am leaving without you.")
        };
        String[] flags = {
            MessageArchive.SENT, MessageArchive.STORED, MessageArchive.DISREGARD,
            MessageArchive.SENT, MessageArchive.STORED
        };
        for (int i = 0; i < testMessages.length; i++) {
            archive.record(testMessages[i], flags[i]);
        }
    }

    @Test
    void testSentMessagesArrayPopulated() {
        assertArrayEquals(new String[]{"Did you get the cake?", "It is dinner time!"}, archive.sentMessages());
    }

    @Test
    void testDisregardedMessagesArrayPopulated() {
        assertArrayEquals(new String[]{"Yohoooo, I am at your gate."}, archive.disregardedMessages());
    }

    @Test
    void testHashAndIdArraysPopulated() {
        String[] hashes = archive.messageHashes();
        String[] ids = archive.messageIds();
        for (int i = 0; i < testMessages.length; i++) {
            assertEquals(testMessages[i].getHash(), hashes[i]);
            assertEquals(testMessages[i].getMessageId(), ids[i]);
        }
    }

    @Test
    void testLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.", archive.findLongestMessage());
    }

    @Test
    void testSearchByMessageId() {
        String expected = String.format("Recipient: 0838884567%nMessage: It is dinner time!");
        assertEquals(expected, archive.findByMessageId(testMessages[3].getMessageId()));
    }

    @Test
    void testSearchByUnknownMessageId() {
        assertEquals("Message ID 9999999999 was not found.", archive.findByMessageId("9999999999"));
    }

    @Test
    void testSearchByRecipient() {
        assertArrayEquals(new String[]{
            "Where are you? You are late! I have asked you to be on time.",
            "Ok, I am leaving without you."
        }, archive.findByRecipient("+27838884567"));
    }

    @Test
    void testDeleteByHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                archive.deleteByHash(testMessages[1].getHash()));
        assertArrayEquals(new String[]{"Ok, I am leaving without you."}, archive.findByRecipient("+27838884567"));
        assertEquals(4, archive.messageIds().length);
    }

    @Test
    void testDeleteByUnknownHash() {
        assertEquals("Message hash 12:3:MISSING was not found.", archive.deleteByHash("12:3:MISSING"));
    }

    @Test
    void testSendersAndRecipients() {
        String list = archive.listSendersAndRecipients();
        assertTrue(list.contains("Sender: +27838968976 | Recipient: +27834557896"));
        assertTrue(list.contains("Sender: +27838968976 | Recipient: +27838884567"));
        assertFalse(list.contains("+27834484567"));
    }

    @Test
    void testReport() {
        String report = archive.generateReport();
        for (Message sent : new Message[]{testMessages[0], testMessages[3]}) {
            assertTrue(report.contains("Message Hash: " + sent.getHash()));
            assertTrue(report.contains("Recipient: " + sent.getRecipient()));
            assertTrue(report.contains("Message: " + sent.getContent()));
        }
        assertFalse(report.contains(testMessages[2].getContent()));
    }

    @Test
    void testReadStoredMessagesFromJson() {
        Message stored = new Message(0, "+27838884567", "Reading this back from the JSON file");
        stored.sentMessage(3);

        boolean found = false;
        for (Message fromFile : archive.readStoredFromJson(Message.STORAGE_FILE)) {
            if (fromFile.getMessageId().equals(stored.getMessageId())) {
                assertEquals(stored.getContent(), fromFile.getContent());
                assertEquals(stored.getHash(), fromFile.getHash());
                found = true;
            }
        }
        assertTrue(found);
    }
}
