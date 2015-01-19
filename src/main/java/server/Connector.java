package server;

import chord.Node;
import client.KeyGenerator;
import com.sun.corba.se.spi.activation.Server;

import java.io.*;
import java.math.BigInteger;

public class Connector {

    private ConnectionHandler connectionHandler;
    private KeyGenerator keyGenerator = new KeyGenerator();
    private String hostName;
    private int port;

    public Connector() {
        connectionHandler = new ConnectionHandler();
    }

    public boolean connectTo(String knownAddress) {
        try {
            String[] split = knownAddress.split(":");
            hostName = split[0];
            port = Integer.parseInt(split[1]);
            connectionHandler.createConnectorSocket(hostName, port);
        } catch (IOException e) {
            System.err.println("Could not connect to " + knownAddress);
            return false;
        }
        return true;
    }


    public String search(BigInteger searchkey) {
        try {
        //    System.out.println("Searching with key:" + keyGenerator.printKey(searchkey));
            ServerMessage serverMessage = new ServerMessage("search");
            serverMessage.setKey(searchkey);
            connectionHandler.writeObject(serverMessage);
            serverMessage = connectionHandler.readObject();
        //    System.out.println("Received search result: " + serverMessage.getAddress());
            return serverMessage.getAddress();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Not found";
    }

    public void newPredecessor(String address) {
        try {
            ServerMessage serverMessage = new ServerMessage("newPredecessor");
            serverMessage.setAddress(address);
            connectionHandler.writeObject(serverMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPredecessor() {
        try {
            ServerMessage serverMessage = new ServerMessage("getPredecessor");
            connectionHandler.writeObject(serverMessage);
            ServerMessage response = connectionHandler.readObject();
            return response.getAddress();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Not found";
    }

    public void notify(String myAddress) {
        try {
            ServerMessage serverMessage = new ServerMessage("notify");
            serverMessage.setAddress(myAddress);
            connectionHandler.writeObject(serverMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSuccessor() {
        try {
            ServerMessage serverMessage = new ServerMessage("getSuccessor");
            connectionHandler.writeObject(serverMessage);
            ServerMessage response = connectionHandler.readObject();
            return response.getAddress();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void checkPredecessor(String myAddress) {
        try {
            ServerMessage serverMessage = new ServerMessage("checkPredecessor");
            serverMessage.setAddress(myAddress);
            connectionHandler.writeObject(serverMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(byte[] message, BigInteger messageKey) {
        try {
            ServerMessage serverMessage = new ServerMessage("storeMessage");
            serverMessage.setMessage(message);
            serverMessage.setKey(messageKey);
            connectionHandler.writeObject(serverMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getMessage(BigInteger messageKey) {
        try {
            ServerMessage serverMessage = new ServerMessage("retrieveMessage");
            serverMessage.setKey(messageKey);
            connectionHandler.writeObject(serverMessage);
            ServerMessage response = connectionHandler.readObject();
            return response.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}

