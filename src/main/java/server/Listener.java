package server;

import chord.Chord;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread {

    private final int port;
    private Chord chord;
    private boolean run = true;

    public Listener(int port, Chord chord) {
        this.port = port;
        this.chord = chord;
    }

    public void run() {
        try {
            waitForCommands();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void waitForCommands() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(port);
        ServerThread serverThread = new ServerThread();
        while (run) {
            Socket clientSocket = serverSocket.accept();
            serverThread.run(clientSocket, chord);
        }
    }

}
