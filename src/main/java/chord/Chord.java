package chord;

import client.KeyComparator;
import client.KeyGenerator;
import server.Connector;
import server.DataHandler;
import server.Listener;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.
        NoSuchElementException;

public class Chord implements ChordInterface {

    private String myHostName;
    private int myPort;
    private String myAddress;
    private BigInteger myNodeKey;
    private Listener listener;
    private Connector connector;
    private FingerTable fingertable;
    private KeyGenerator keyGenerator;
    private Node predecessor;
    private Node successor;
    private DataHandler storage;


    public Chord(String address) {
        setAddress(address);
        connector = new Connector();
        keyGenerator = new KeyGenerator();
        myNodeKey = keyGenerator.generateSHAint(address);
        fingertable = new FingerTable(new Node(address), this);
        listener = new Listener(myPort, this);
        storage = new DataHandler();
    }

    public void setAddress(String address) {
        String[] split = address.split(":");
        myHostName = split[0];
        myPort = Integer.parseInt(split[1]);
        myAddress = address;
    }

    @Override
    public BigInteger joinChord(String knownAddress) {
        if (knownAddress == null) {
            createChordNetWork();
        } else {
            joinChordNetWork(knownAddress);
        }

        startStabilizer();
        return keyGenerator.generateSHAint(myHostName);
    }

    private void createChordNetWork() {
        setSuccessor(myAddress);
        listener.start();
    }

    private void joinChordNetWork(String knownAddress) {
        listener.start();
        connector.connectTo(knownAddress);
        String successorAddress = connector.search(myNodeKey);
        setSuccessor(successorAddress);
    }

    public void startStabilizer() {
        Stabilizer stabilizer = new Stabilizer(this);
        stabilizer.start();
    }

    @Override
    public void stabilize() {
        if(!isAlive(successor.getAddress())) {
            String successorAddress = findSuccessor(myNodeKey.add(BigInteger.ONE));
            successor = new Node(successorAddress);
        }
        connector.connectTo(successor.getAddress());
        String successorsPredecessor = connector.getPredecessor();
        KeyComparator comparer = new KeyComparator(myNodeKey, successor.getKey());
        if(!successorsPredecessor.equals("nil") && comparer.checkInterval(new Node(successorsPredecessor).getKey())) {
            setSuccessor(successorsPredecessor);
        }
    }

    public void notifySuccessor() {
        connector.connectTo(successor.getAddress());
        connector.notify(myAddress);
    }

    @Override
    public String search(BigInteger searchKey) {
        try {
            checkMyInterval(searchKey);
            return findSuccessor(searchKey);
        } catch (NoSuchElementException e) {
            return myAddress;
        } catch (KeyAlreadyExistsException e) {
            return successor.getAddress();
        }
    }

    private void checkMyInterval(BigInteger key) throws KeyAlreadyExistsException{
        KeyComparator comparer = new KeyComparator(myNodeKey, successor.getKey());
        if(comparer.checkInterval(key)) {
            throw new KeyAlreadyExistsException();
        }
    }

    @Override
    public String findSuccessor(BigInteger key) throws NoSuchElementException, KeyAlreadyExistsException {
        Node node = findClosestPreceding(key);
        if(node.getAddress().equals(myAddress)) {
            return node.getAddress();
        } else {
            return connectAndSearch(node.getAddress(), key);
        }
    }

    private Node findClosestPreceding(BigInteger key) {
        Node self = new Node(myAddress);
        for (int i = 3; i >= 0; i--) {
            Node finger = fingertable.getNode(i);
            KeyComparator comparer = new KeyComparator(myNodeKey, key);
            if (comparer.checkInterval(finger.getKey())) {
                if(isAlive(finger.getAddress())){
                    return finger;
                }
            }
        }
        return self;
    }

    public String connectAndSearch(String address, BigInteger searchkey) {
        connector.connectTo(address);
        return connector.search(searchkey);
    }

    @Override
    public void printNodeInfo() {
        NodePrinter printer = new NodePrinter();
        printer.printSelf(myNodeKey, myAddress);
        printer.printSuccessor(successor);
        printer.printPredecessor(predecessor);
        printer.printFingerTable(fingertable.toString());
    }

    @Override
    public BigInteger pushMessage(byte[] message, BigInteger testKey) {
       // BigInteger objectKey = keyGenerator.generateSHAint(message.toString());
       //  BigInteger objectKey = new BigInteger(testKey);
        System.out.println("storing message with key " + testKey.toString());
        System.out.println("visible key " + KeyGenerator.printKey(testKey));
        String address = search(testKey);
        System.out.println("Storing message at " + address);
        connector.connectTo(address);
        connector.sendMessage(message, testKey);
        return testKey;
    }

    public byte[] getMessage(String searchKey) {
        BigInteger key = new BigInteger(searchKey);
        System.out.println("searching with " + key);
        System.out.println("visible key " + KeyGenerator.printKey(key));
        String address = search(key);
        System.out.println(address + " has the message, connecting");
        connector.connectTo(address);
        return connector.getMessage(new BigInteger(searchKey));
    }

    @Override
    public void checkPredecessor() {
        if(predecessor != null) {
            if(!isAlive(predecessor.getAddress())) {
                setPredecessor(null);
            }
        }
    }

    public boolean isAlive(String address) {
        if(connector.connectTo(address)) {
            connector.checkPredecessor(myAddress);
            return true;
        } else {
            return false;
        }
    }

    public void setPredecessor(String address) {
        if(address == null) {
            predecessor = null;
        } else {
            predecessor = new Node(address);
            moveMessages(predecessor);
        }
    }

    public void setSuccessor(String address) {
        if(address == null) {
            predecessor = null;
        } else {
            successor = new Node(address);
            fingertable.insert(0, successor);
            moveMessages(successor);
        }
    }

    private void moveMessages(Node node) {
        Map<BigInteger, byte[]> messages = storage.checkSuitable(myNodeKey, node.getKey());
        if(!messages.isEmpty()) {
            System.out.println("Found suitable neighbor, pushing some messages to: " + node.getAddress() + " with nodeId: " + node.getKey());
            for (BigInteger key : messages.keySet()) {
                System.out.println("pushing message with key: " + key);
                connector.connectTo(node.getAddress());
                connector.sendMessage(messages.get(key), key);
            }
        }
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public Node getSuccessor() {
        return successor;
    }

    public BigInteger getMyNodeKey() {
        return myNodeKey;
    }

    public String getMyAddress() {
        return myAddress;
    }

    public DataHandler getStorage() {
        return storage;
    }

}
