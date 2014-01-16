package ru.jts.server.network.serverpackets;

import ru.jts.common.network.udp.ServerPacket;
import ru.jts.server.network.Client;

/**
 * @author: Camelion
 * @date: 17.01.14/3:06
 */
public class InvalidPassword extends ServerPacket<Client> {
    private final short sessionId;

    public InvalidPassword(short sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    protected void writeImpl() {
        writeShort(0x00);
        writeByte(0xFF);
        writeByte(0x17);
        writeBytes(0x00, 0x00, 0x00);
        writeShort(sessionId);
        writeBytes(0x00, 0x00);
        writeByte(0x43);
        writeString("Invalid password.");
    }
}
