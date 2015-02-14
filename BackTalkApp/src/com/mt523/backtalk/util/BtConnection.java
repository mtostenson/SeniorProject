package com.mt523.backtalk.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BtConnection {

    private final int PORT = 4242;
    private final String ADDRESS = "ec2-54-153-115-148.us-west-1.compute.amazonaws.com";

    private InetAddress inetAddress;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public BtConnection() {
        try {
            inetAddress = InetAddress.getByName(ADDRESS);
            socket = new Socket(inetAddress, PORT);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

}