package client;


import proto.Messages;
import protobuf.InvalidProtocolBufferException;

import java.io.IOException;

public class MessageParser {

    public MessageParser() {
    }

    public void parseMessage(byte[] messageBytes) throws InvalidProtocolBufferException {
        Messages.AMessage message = Messages.AMessage.parseFrom(messageBytes);
        printMessage(message);
    }

    public void parseMessageList(byte[] message) throws IOException {
        Messages.MessageList list = Messages.MessageList.parseFrom(message);
        checkEmptyResponse(list);
        for(int i=0; i < list.getMessagesCount(); i++) {
            printMessage(list.getMessages(i));
        }
    }

    public void parseByteMessageList(byte[] bytes) throws InvalidProtocolBufferException {
        Messages.MessageList list = Messages.MessageList.parseFrom(bytes);
        checkEmptyResponse(list);
        for(int i=0; i < list.getMessagesCount(); i++) {
            printMessage(list.getMessages(i));
        }
    }

    private void checkEmptyResponse(Messages.MessageList list) {
        if(list.getMessagesCount() < 1) {
            System.out.println("No messages received.");
        }
    }

    private static void printMessage(Messages.AMessage message) {
        System.out.println("Message: ");
        System.out.println("Sender: " + message.getSender());
        System.out.println("Topic: " + message.getTopic());
        System.out.println("Content: " + message.getContent());
        System.out.println("Recipient: " + message.getRecipient());
        System.out.println("Timestamp: " + message.getTimeStamp());
        System.out.println("Id: " + message.getId());
    }

}
