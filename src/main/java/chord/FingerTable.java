package chord;

import client.KeyGenerator;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hugo on 12/8/14.
 */
public class FingerTable extends Thread {

    private ConcurrentHashMap<Integer, Node> fingers = new ConcurrentHashMap<>();
    private Chord chord;
    private boolean run = true;
    private KeyGenerator keyGenerator = new KeyGenerator();
    private static final int KEYSPACE = 160;

    public FingerTable(Node node, Chord chord) {
        this.chord = chord;
        for(int i = 0; i < KEYSPACE; i++) {
            fingers.put(i, node);
        }
    }

    public void run() {
        while(run) {
            try {
                Thread.sleep(300);
                if(!fingers.get(0).getKey().equals(chord.getMyNodeKey())) {
                    calculateFingers();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void calculateFingers() {
        for (int i = 1; i < KEYSPACE; i++) {
            BigInteger searchKey = calculateIndex(i);
          //  System.out.println("searching with key: " + searchKey + " on index " + i);
            String address = chord.search(searchKey);
            compareSearchResult(i, address);
        }
    }

    private BigInteger calculateIndex(int index) {
        BigInteger two = new BigInteger("2");
        BigInteger mod = two.pow(KEYSPACE);
        BigInteger tempIndex = chord.getMyNodeKey().add(two.pow(index));
        return tempIndex.mod(mod);
    }

    private void compareSearchResult(int index, String address) {
        if(!fingers.get(index).getAddress().equals(address)) {
            fingers.replace(index, new Node(address));
          //  testPrint(index, address);
        }
    }

    private void testPrint(int index, String address) {
        System.out.println("New finger found on index: " + index + ", added address: " + address);
        chord.printNodeInfo();
    }

    public void insert(int index, Node node) {
        fingers.replace(index, node);
    }

    public Node getNode(int index) {
        return fingers.get(index);
    }

    @Override
    public String toString() {
        String fingerTableString = "\n";
        for(int i = 0; i < KEYSPACE; i ++) {
            Node node = fingers.get(i);
            fingerTableString = fingerTableString + "#" + i + "  Address: " + node.getAddress() + "  Key: " + keyGenerator.printKey(node.getKey()) + "\n";
        }
        return fingerTableString;
    }
}
