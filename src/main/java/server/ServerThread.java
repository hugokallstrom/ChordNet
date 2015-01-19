package server;

import chord.Chord;
import chord.Node;
import client.KeyComparator;
import client.KeyGenerator;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

public class ServerThread extends Thread {

    private ConnectionHandler connectionHandler;
    private Chord chord;
    private KeyGenerator keyGenerator = new KeyGenerator();

    public ServerThread() {

    }

    public void run(Socket clientSocket, Chord chord) {
        try {
            this.chord = chord;
            connectionHandler = new ConnectionHandler();
            connectionHandler.createStreams(clientSocket);
            ServerMessage serverMessage = connectionHandler.readObject();
            String command = serverMessage.getCommand();
            switchCommands(command, serverMessage);
        } catch (IOException e) {
            System.err.println("IOException when creating a socket listener.");
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found when creating a socket listener.");
        }
    }

    public void switchCommands(String command, ServerMessage serverMessage) throws IOException {
        switch (command) {
            case "search": search(serverMessage);
                break;
            case "newPredecessor": newPredecessor(serverMessage);
                break;
            case "getPredecessor": getPredecessor(serverMessage);
                break;
            case "notify": notify(serverMessage);
                break;
            case "getSuccessor": getSuccessor(serverMessage);
                break;
            case "checkPredecessor": checkPredecessor(serverMessage);
                break;
            case "storeMessage": storeMessage(serverMessage);
                break;
            case "retrieveMessage": retrieveMessage(serverMessage);
        }
    }

    private void retrieveMessage(ServerMessage serverMessage) throws IOException {
        BigInteger messageKey = serverMessage.getKey();
        byte[] message = chord.getStorage().getMessage(messageKey);
        serverMessage.setMessage(message);
        connectionHandler.writeObject(serverMessage);
    }

    private void storeMessage(ServerMessage serverMessage) {
        byte[] message = serverMessage.getMessage();
        BigInteger messageKey = serverMessage.getKey();
        chord.getStorage().addMessage(messageKey, message);
    }

    private void search(ServerMessage serverMessage) throws IOException {
        BigInteger searchKey = serverMessage.getKey();
     //   System.out.println("recieved search req with key: " + keyGenerator.printKey(searchKey));
        String result = chord.search(searchKey);
     //   System.out.println("Sending back search result: " + result);
        serverMessage.setAddress(result);
        connectionHandler.writeObject(serverMessage);
    }

    private void newPredecessor(ServerMessage serverMessage) throws IOException {
        String address = serverMessage.getAddress();
        chord.setPredecessor(address);
        chord.printNodeInfo();
        connectionHandler.writeObject(serverMessage);
    }

    private void getPredecessor(ServerMessage serverMessage) throws IOException {
        if(chord.getPredecessor() == null) {
            serverMessage.setAddress("nil");
        } else {
            String predecessorAddress = chord.getPredecessor().getAddress();
            serverMessage.setAddress(predecessorAddress);
        }
        connectionHandler.writeObject(serverMessage);
    }

    private void notify(ServerMessage serverMessage) throws IOException {
        String newPredecessor = serverMessage.getAddress();
        if(chord.getPredecessor() == null) {
            chord.setPredecessor(newPredecessor);
        } else {
            KeyComparator comparer = new KeyComparator(chord.getPredecessor().getKey(), chord.getMyNodeKey());
            if(comparer.checkInterval(new Node(newPredecessor).getKey())) {
                chord.setPredecessor(newPredecessor);
            }
        }
    }

    private void getSuccessor(ServerMessage serverMessage) throws IOException {
        String successorAddress = chord.getSuccessor().getAddress();
        serverMessage.setAddress(successorAddress);
        connectionHandler.writeObject(serverMessage);
    }

    private void checkPredecessor(ServerMessage serverMessage) {

    }

}
