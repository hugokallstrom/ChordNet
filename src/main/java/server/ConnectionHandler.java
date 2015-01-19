package server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ConnectionHandler {

    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream oout;
    private ObjectInputStream oin;

    public void createConnectorSocket(String hostName, int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(hostName, port), 500);
        createStreams(socket);
    }

   public void createStreams(Socket socket) throws IOException {
       out = new DataOutputStream(socket.getOutputStream());
       in = new DataInputStream(socket.getInputStream());
       oout = new ObjectOutputStream(out);
       oin = new ObjectInputStream(in);
   }

    public void writeObject(ServerMessage serverMessage) throws IOException {
        oout.writeObject(serverMessage);
        oout.flush();
    }

    public ServerMessage readObject() throws IOException, ClassNotFoundException {
        ServerMessage serverMessage = (ServerMessage) oin.readObject();
        return serverMessage;
    }
}
