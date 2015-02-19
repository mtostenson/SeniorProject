package com.mt523.backtalk.packets.server;

public class CardRequest extends ServerPacket {

    private int id;
    
    public CardRequest(int id) {
        this.id = id;
    }
    
    @Override
    public void handlePacket() {
        server.serveImage(id);
    }

}
