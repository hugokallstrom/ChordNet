package chord;


public class Stabilizer extends Thread {

    private Chord chord;
    private boolean run = true;
    private FingerTable fingerTable;

    public Stabilizer(Chord chord) {
        this.chord = chord;
        fingerTable = new FingerTable(new Node(chord.getMyAddress()), chord);
    }

    public void run() {
        try {
            while(run) {
                Thread.sleep(5000);
                checkForNewSuccessor();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkForNewSuccessor() throws InterruptedException {
        if(chord.getSuccessor().getKey().equals(chord.getMyNodeKey()) && chord.getPredecessor() != null) {
            chord.setSuccessor(chord.getPredecessor().getAddress());
        }
        chord.stabilize();
        chordNotify();
        fixFingers();
        chord.checkPredecessor();
  //      chord.printNodeInfo();
    }

    private void fixFingers() {
        if(!fingerTable.getNode(0).getKey().equals(chord.getMyNodeKey())) {
            fingerTable.calculateFingers();
        }
    }

    private void chordNotify() {
        if(!chord.getSuccessor().getKey().equals(chord.getMyNodeKey()) && chord.isAlive(chord.getSuccessor().getAddress())) {
            chord.notifySuccessor();
        }
    }

}

