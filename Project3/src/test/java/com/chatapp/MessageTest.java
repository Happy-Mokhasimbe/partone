package com.chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MessageTest {

    // Test case 1 and 2 from the brief, IDs set so hashes are predictable
    private final Message testCase1 = new Message("0098765432", 0, "+27718693002",
            "Hi Mike, can you join us for dinner tonight?");
    private final Message testCase2 = new Message("0012121212", 1, "08575975889",
            "Hi Keegan, did you receive the payment?");

    @Test
    void testMessageUnder250() {
        assertEquals("Message ready to send.", testCase1.checkMessageLength());
    }

    @Test
    void testMessageOver250() {
        Message longOne = new Message("5555555555", 0, "+27718693002", "b".repeat(300));
        assertEquals("Message exceeds 250 characters by 50; please reduce the size.", longOne.checkMessageLength());
    }

    @Test
    void testRecipientSuccess() {
        assertEquals("Cell phone number successfully captured.", testCase1.checkRecipientCell());
    }

    @Test
    void testRecipientFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                testCase2.checkRecipientCell());
    }

    @Test
    void testHashForTestCase1() {
        assertEquals("00:0:HITONIGHT", testCase1.createMessageHash());
    }

    @Test
    void testHashesInLoop() {
        Message[] messages = {
            testCase1,
            testCase2,
            new Message("7700000000", 2, "+27838884567", "Where are you? You are late! I have asked you to be on time.")
        };
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT", "77:2:WHERETIME"};

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash());
        }
    }

    @Test
    void testMessageIdCreated() {
        Message message = new Message(0, "+27718693002", "Testing message ID");
        assertTrue(message.checkMessageID());
        assertEquals("Message ID generated: " + message.getMessageId(), message.printMessageID());
    }

    @Test
    void testSendSelected() {
        Message message = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    void testDisregardSelected() {
        assertEquals("Press 0 to delete the message.", testCase2.sentMessage(2));
    }

    @Test
    void testStoreSelected() {
        Message message = new Message(0, "+27718693002", "Call me when you land");
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    @Test
    void testReturnTotalMessages() {
        int before = Message.returnTotalMessages();

        Message sendMe = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        Message discardMe = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        sendMe.sentMessage(1);
        discardMe.sentMessage(2);

        assertEquals(before + 1, Message.returnTotalMessages());
        assertTrue(Message.printMessages().contains(sendMe.getMessageDetails()));
    }
}
