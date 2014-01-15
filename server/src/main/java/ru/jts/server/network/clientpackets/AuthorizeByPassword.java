package ru.jts.server.network.clientpackets;

import ru.jts.common.network.udp.ClientPacket;
import ru.jts.common.util.ArrayUtils;
import ru.jts.server.network.Client;
import ru.jts.server.network.serverpackets.AuthorizeByPasswordResponse;

/**
 * @author: Camelion
 * @date: 02.11.13/0:43
 */
public class AuthorizeByPassword extends ClientPacket<Client> {
    private final short sessionId;

    public AuthorizeByPassword(short sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void readImpl() {
        byte unk1 = readByte();
        // JSON Message
        // session: = md5Hex of Client HWID
        String serializedSession = readBrackets();
        byte passLength = readByte();
        String pass = readString(passLength); // plain pass
        byte blowFishLength = readByte();
        byte[] blowFish = readBytes(blowFishLength);
        readBytes(16); // unknown
        short unk2 = readShort(); // неизвестно, изменяется при каждом новом подключении
        readShort(); // 0

        getClient().sendPacket(new AuthorizeByPasswordResponse(sessionId));


        System.out.println(ArrayUtils.bytesToHexString(content.copy(content.readerIndex(), content.readableBytes()).array()));
    }
}
