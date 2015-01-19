package client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by hugo on 11/17/14.
 */
public class Message {

    private String sender;
    private String topic;
    private String content;
    private String recipient;
    private String id;
    private String timestamp;

    public Message() {
        generateId();
        generateTimeStamp();
    }

    public void setSender(String sender) throws IllegalArgumentException {
        if(!validateField(sender)) {
            throw new IllegalArgumentException("Sender needs to be between 1-255 characters");
        } else {
            this.sender = sender;
        }
    }

    public String getSender() {
        return sender;
    }

    public void setTopic(String topic) throws IllegalArgumentException {
        if(!validateField(topic)) {
            throw new IllegalArgumentException("Topic needs to be between 1-255 characters");
        } else {
            this.topic = topic;
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setContent(String content) throws IllegalArgumentException {
        if(!validateContent(content)) {
            throw new IllegalArgumentException("Content needs to be between 0-65536 characters");
        } else {
            this.content = content;
        }
    }

    public String getContent() {
        return content;
    }

    public void setRecipient(String recipient) throws IllegalArgumentException {
        if(!validateContent(recipient)) {
            throw new IllegalArgumentException("Recipient needs to be between 1-255 characters");
        } else {
            this.recipient = recipient;
        }
    }

    public String getRecipient() {
        return recipient;
    }

    private void generateTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:mm:ss a");
        timestamp = sdf.format(date);
    }

    private void generateId() {
        id =  UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    private boolean validateField(String field) {
        return (field.length() > 1 || field.length() < 255);
    }

    private boolean validateContent(String content) {
        return (content.length() > 0 || content.length() < 65536);
    }

}
