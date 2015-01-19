package client;

import chord.Chord;
import protobuf.InvalidProtocolBufferException;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;


public class ClientMain {

    private static ArgumentValidator validator;
    private static Chord chord;

    public static void main(String[] args) {
        try {
            validator = new ArgumentValidator();
            validator.validateProgramArguments(args);
            createClient(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createClient(String[] args) {
        if (args.length == 1) {
            chord = new Chord(args[0]);
            chord.joinChord(null);
        } else if (args.length == 2) {
            chord = new Chord(args[0]);
            chord.joinChord(args[1]);
        }
        try {
            runTests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runTests() throws IOException {
        String input = "run";
        System.out.println("Welcome to ChordNet! For help, write 'help'. Enjoy.");
        while (!input.equals("quit")) {
            BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
            input = br.readLine();
            switch (input) {
                case "send":
                    postMessage();
                    break;
                case "get":
                    getAndPrintMessage();
                    break;
                case "info":
                    chord.printNodeInfo();
                    break;
                case "help":
                    printHelp();
                    break;
            }
        }
    }

    private static void printHelp() {
        System.out.println("send: sends a message");
        System.out.println("get: retrieves a message with a specified key");
        System.out.println("info: prints info about current node");
        System.out.println("quit: exits the program");
    }

    private static void postMessage() throws IOException {
        System.out.println("Enter name for message:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String testName = br.readLine();
        KeyGenerator keygen = new KeyGenerator();
        BigInteger key = chord.pushMessage(createMessageFromArgs(), keygen.generateSHAint(testName));
        System.out.println("Message posted with key: " + key.toString());
        System.out.println("visible key " + KeyGenerator.printKey(key));
    }

    public static void getAndPrintMessage() throws IOException {
        System.out.println("Enter search key for message:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String searchKey = br.readLine();
        byte[] message = chord.getMessage(searchKey);
        MessageParser messageParser = new MessageParser();
        try {
            messageParser.parseMessage(message);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private static byte[] createMessageFromArgs() {
        ProtoMessage protoMessage = new ProtoMessage();
        try {
            protoMessage.setSender("Hugo");
            protoMessage.setTopic("Topic");
            protoMessage.setContent("Hej hej hemskt mycket hej");
            protoMessage.setRecipient("Danne");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return protoMessage.getMessage().toByteArray();
    }
}
