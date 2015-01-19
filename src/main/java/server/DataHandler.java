package server;

import client.KeyComparator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hugo on 12/15/14.
 */
public class DataHandler {

    private final Map<BigInteger, byte[]> byteMessages;
    private final Map<BigInteger, byte[]> publicKeys;
    private final Map<BigInteger, byte[]> signatures;
    private final Map<BigInteger, byte[]> aesKeys;
    private final Map<BigInteger, byte[]> shiftKeys;

    public DataHandler() {
        byteMessages = new ConcurrentHashMap<>();
        publicKeys = new ConcurrentHashMap<>();
        signatures = new ConcurrentHashMap<>();
        aesKeys = new ConcurrentHashMap<>();
        shiftKeys = new ConcurrentHashMap<>();
    }

    public void addMessage(BigInteger messageKey, byte[] message) {
        byteMessages.put(messageKey, message);
        printCurrent();
    }

    private void printCurrent() {
        System.out.println("Current messages: ");
        for (BigInteger bigInteger : byteMessages.keySet()) {
            System.out.println("Message with key: " + bigInteger.toString());
        }
    }

    public byte[] getMessage(BigInteger messageKey) {
        return byteMessages.get(messageKey);
    }

    public Map<BigInteger, byte[]> getByteMessages() {
        return byteMessages;
    }

    public Map<BigInteger, byte[]> getPublicKeys() {
        return publicKeys;
    }

    public Map<BigInteger, byte[]> getSignatures() {
        return signatures;
    }

    public Map<BigInteger, byte[]> getAesKeys() {
        return aesKeys;
    }

    public Map<BigInteger, byte[]> getShiftKeys() {
        return shiftKeys;
    }

    public Map<BigInteger, byte[]> checkSuitable(BigInteger myKey, BigInteger neighborKey) {
        Map<BigInteger, byte[]> messagesToPush = new ConcurrentHashMap<>();
        for (BigInteger messageKey : byteMessages.keySet()) {
            KeyComparator keyComparator = new KeyComparator(myKey, neighborKey);
            if(keyComparator.checkInterval(messageKey)) {
                messagesToPush.put(messageKey, byteMessages.get(messageKey));
            }
        }
        return messagesToPush;
    }
}
