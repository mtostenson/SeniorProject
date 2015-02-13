package com.mt523.backtalk;

import java.net.*;
import java.io.*;
import com.mt523.backtalk.packets.BasePacket;

class TestClient {

	private Socket socket;
	private InetAddress addr;
	private final int PORT = 4242;
	ObjectOutputStream output;
	ObjectInputStream input;

	public TestClient()  {
	
		try {
			addr = InetAddress.getByName("ec2-54-153-115-148.us-west-1.compute.amazonaws.com");
			socket = new Socket(addr,PORT);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			while(true) {
				((BasePacket)input.readObject()).handlePacket();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		new TestClient();
	}
}
