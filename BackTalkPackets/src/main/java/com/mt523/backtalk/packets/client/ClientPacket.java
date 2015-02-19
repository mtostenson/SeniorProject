package com.mt523.backtalk.packets.client;

import com.mt523.backtalk.packets.IBackTalkPacket;

public abstract class ClientPacket implements IBackTalkPacket {

    IBackTalkClient client;
    
    public void setClient(IBackTalkClient client) {
        this.client = client;
    }

}