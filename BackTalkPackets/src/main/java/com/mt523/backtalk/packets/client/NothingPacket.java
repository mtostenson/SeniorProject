package com.mt523.backtalk.packets.client;

public class NothingPacket extends ClientPacket {

    @Override
    public void handlePacket() {
        System.out.println("Server says do nothing.");
    }

}
