package chord;

import java.math.BigInteger;

/*
 * We expect you to implement the following chord procedures.
 * We do not care much about the return types and/or the parameters for the suggested methods as long as it works
 * You should provide a small interface to enable us to start the network, to join the network from another host, to search the Chord ring for objects, show the fingers table of a node and push things in to the network.
 * Make absolutely no assumptions about the computers we use for testing. We might use our home computers to start a network and our children's computers as joining nodes
 *  Our suggestion is that you make your interfaces using either sockets or REST.
 *  If you use sockets, feel free to reuse code from previous courses
 *  If you intend to use REST, then each client will run both the client and the server REST code at the same time.
 *  For an example system doing that: http://tools.ietf.org/html/draft-jimenez-distributed-resource-directory-00.html
 */
public interface ChordInterface {
    /*
    * method to join a Chord network or start a new network
    * param1 mykey: is the name of the new peer joining the network (typically your own computer)
    * param2  personIknowKey: My entry point to the Chord ring. If null is passed, then a new ring is created
    * return new ring ID if new ring is created
    */
    public BigInteger joinChord(String personIknowKey);

    /*
     * Is my predecessor dead?
     */
    public void checkPredecessor();

    /*
     * Who is my first living successor in my my successor's list?
     */
    public void stabilize();

    /*
     * Ask someone you know about the successor of a node
     * param1 someNodeKey: The node looking for its successor
     * param2 personIknowKey: The node that is being asked
     */
    public String findSuccessor(BigInteger key);

    /*
     * Search the Chord network for an object with a given hash
     * param1 IamLookingForThis: The hash of the object Name
     */
    public String search(BigInteger searchKey);

    /*
     * Print the successor's list , the fingers and the predecessor's list of a node (one single method
     * param1 nodeKey: the node for which the data is printed
     */
    public void printNodeInfo();

    /*
     * Add an object to be stored
     * param1 objectKey: the hash of the (name of the) object
     */
    public BigInteger pushMessage(byte[] message, BigInteger testKey);

}