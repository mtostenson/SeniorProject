package com.mt523.backtalk.packets.client;

import java.util.Vector;

import com.mt523.backtalk.packets.IBackTalkPacket;

public abstract class ClientPacket implements IBackTalkPacket {

    transient IBackTalkClient client;

    public void setClient(IBackTalkClient client) {
        this.client = client;
    }

    public interface IBackTalkClient {

        public void setDeck(Vector<Card> deck);

    }
}