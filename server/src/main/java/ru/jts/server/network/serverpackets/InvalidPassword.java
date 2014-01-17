package ru.jts.server.network.serverpackets;

import ru.jts.common.network.udp.ServerPacket;
import ru.jts.common.util.ArrayUtils;
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

        writeShort(sessionId);
        writeShort(0x00);
        writeByte(0x43); // LOGIN_REJECTED_INVALID_PASSWORD
        writeString("Invalid password.");

        System.out.println(ArrayUtils.bytesToHexString(content.copy().array()));
    }
}
