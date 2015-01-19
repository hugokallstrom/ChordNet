package chord;

import client.KeyGenerator;

import java.math.BigInteger;

/**
 * Created by hugo on 12/4/14.
 */
public class Node {
    private String address;
    private BigInteger key;

    public Node(String address) {
        if(address == null) {
            this.address = null;
            this.key = null;
        } else {
            KeyGenerator keyGenerator = new KeyGenerator();
            this.address = address;
            this.key = keyGenerator.generateSHAint(address);
        }
    }

    public String getAddress() {
        return address;
    }

    public BigInteger getKey() {
        return key;
    }
}
