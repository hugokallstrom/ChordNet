package client;

import proto.Messages;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by hugo on 11/17/14.
 */
public class ProtoMessage {

    private Messages.AMessage.Builder messageBuilder;

    public ProtoMessage() {
        messageBuilder = Messages.AMessage.newBuilder();
        generateId();
        generateTimeStamp();
    }

    public void setSender(String sender) throws IllegalArgumentException {
        if(!validateField(sender)) {
            throw new IllegalArgumentException("Sender needs to be between 1-255 characters");
        } else {
            messageBuilder.setSender(sender);
        }
    }

    public String getSender() {
        return messageBuilder.getSender();
    }

    public void setTopic(String topic) throws IllegalArgumentException {
        if(!validateField(topic)) {
            throw new IllegalArgumentException("Topic needs to be between 1-255 characters");
        } else {
            messageBuilder.setTopic(topic);
        }
    }

    public String getTopic() {
        return messageBuilder.getTopic();
    }

    public void setContent(String content) throws IllegalArgumentException {
        if(!validateContent(content)) {
            throw new IllegalArgumentException("Content needs to be between 0-65536 characters");
        } else {
            messageBuilder.setContent(content);
        }
    }

    public String getContent() {
        return messageBuilder.getContent();
    }

    public void setRecipient(String recipient) throws IllegalArgumentException {
        if(!validateContent(recipient)) {
            throw new IllegalArgumentException("Recipient needs to be between 1-255 characters");
        } else {
            messageBuilder.setRecipient(recipient);
        }
    }

    public String getRecipient() {
        return messageBuilder.getRecipient();
    }

    private void generateTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:mm:ss a");
        messageBuilder.setTimeStamp(sdf.format(date));
    }

    private void generateId() {
        String id =  UUID.randomUUID().toString();
        messageBuilder.setId(id);
    }

    public String getId() {
        return messageBuilder.getId();
    }

    public Messages.AMessage getMessage() {
        return messageBuilder.build();
    }

    private boolean validateField(String field) {
        return (field.length() > 1 || field.length() < 255);
    }

    private boolean validateContent(String content) {
        return (content.length() > 0 || content.length() < 65536);
    }

}
