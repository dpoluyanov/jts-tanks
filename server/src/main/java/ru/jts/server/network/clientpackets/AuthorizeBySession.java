package ru.jts.server.network.clientpackets;

import ru.jts.common.log.Log;
import ru.jts.common.network.udp.ClientPacket;
import ru.jts.server.network.Client;

/**
 * @author: Camelion
 * @date: 12.11.13/14:06
 */
public class AuthorizeBySession extends ClientPacket<Client> {
    @Override
    public void readImpl() {
        String serializedSession = readBrackets();
        Log.w(serializedSession);
        byte _0x7D = readByte();
        byte unk = readByte(); // 0x00
        byte blowFishLength = readByte();
        byte[] blowFish = readBytes(blowFishLength);
        readBytes(16); // unknown
        short unk2 = readShort(); // неизвестно, изменяется при каждом новом подключении
        readShort(); // 0

        //System.out.println(ArrayUtils.bytesToHexString(content.copy(content.readerIndex(), content.readableBytes()).array()));
    }
}
