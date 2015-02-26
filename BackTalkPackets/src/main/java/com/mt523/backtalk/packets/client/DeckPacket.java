package com.mt523.backtalk.packets.client;

import java.util.Vector;

public class DeckPacket extends ClientPacket {

    private Vector<Card> deck;

    public DeckPacket(Vector<Card> deck) {
        this.deck = deck;
    }

    @Override
    public void handlePacket() {
        client.setDeck(deck);
    }

}
