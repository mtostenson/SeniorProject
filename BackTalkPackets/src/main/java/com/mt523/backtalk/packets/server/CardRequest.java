package com.mt523.backtalk.packets.server;

public class CardRequest extends ServerPacket {

    public enum CardTier { DEFAULT, PAID1, PAID2 }
    
    private CardTier tier;    
    
    public CardRequest(CardTier tier) {
        this.tier = tier;
    }
    
    @Override
    public void handlePacket() {
        server.serveCards(tier);
    }

}
