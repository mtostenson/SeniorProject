package com.mt523.backtalk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerWorker extends Thread {
    
    Socket socket;
    ObjectOutputStream output;
    ObjectInputStream input;

    ServerWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
