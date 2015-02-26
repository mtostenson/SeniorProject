
package com.mt523.backtalk.packets.server;

import com.mt523.backtalk.packets.IBackTalkPacket;
import com.mt523.backtalk.packets.server.CardRequest.CardTier;

public abstract class ServerPacket implements IBackTalkPacket {

    transient IBackTalkServer server;
    
    public void setServer(IBackTalkServer server) {
        this.server = server;
    }
    
    public interface IBackTalkServer {
        public void serveCards(CardTier tier);
    }

}               