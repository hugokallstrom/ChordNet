package chord;

import client.KeyGenerator;

import java.math.BigInteger;

/**
 * Created by hugo on 12/12/14.
 */
public class NodePrinter {

    private KeyGenerator keyGenerator = new KeyGenerator();

    public void printSelf(BigInteger myNodeKey, String myAddress) {
        System.out.println("Info about node with key: " + keyGenerator.printKey(myNodeKey) + " and address: " + myAddress);
    }

    public void printSuccessor(Node successor) {
        if(successor == null) {
            System.out.println("Successor: No successor");
        } else {
            System.out.println("Successor: " + keyGenerator.printKey(successor.getKey()) + " " + successor.getAddress());
        }
    }

    public void printPredecessor(Node predecessor) {
        if(predecessor == null) {
            System.out.println("Predecessor: No predecessor");
        } else {
            System.out.println("Predecessor: " + keyGenerator.printKey(predecessor.getKey())  + " " + predecessor.getAddress());
        }
    }

    public void printFingerTable(String fingertable) {
        System.out.println("Finger table: " + fingertable);
    }
}
