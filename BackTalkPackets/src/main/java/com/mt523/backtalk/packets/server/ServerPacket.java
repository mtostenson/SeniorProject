package com.mt523.backtalk.packets.server;

import com.mt523.backtalk.packets.IBackTalkPacket;

public abstract class ServerPacket implements IBackTalkPacket {

    IBackTalkServer server;
    
    public void setServer(IBackTalkServer server) {
        this.server = server;
    }

}