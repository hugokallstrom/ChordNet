package server;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by hugo on 12/8/14.
 */
public class ServerMessage implements Serializable {

    private String command;
    private BigInteger key;
    private String address;
    private byte[] message;

    public ServerMessage (String command) {
        this.command = command;
    }

    public BigInteger getKey() {
        return key;
    }

    public void setKey(BigInteger key) {
        this.key = key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommand() {
        return command;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public byte[] getMessage() {
        return message;
    }
}
