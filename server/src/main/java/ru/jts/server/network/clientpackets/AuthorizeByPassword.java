package ru.jts.server.network.clientpackets;

import ru.jts.common.math.Rnd;
import ru.jts.common.network.udp.ClientPacket;
import ru.jts.common.util.ArrayUtils;
import ru.jts.server.network.Client;
import ru.jts.server.network.serverpackets.AuthorizeResponse;

import java.util.Map;

/**
 * @author: Camelion
 * @date: 02.11.13/0:43
 */
public class AuthorizeByPassword extends ClientPacket<Client> {
    private final short sessionId;
    private byte[] blowFishKey;

    public AuthorizeByPassword(short sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void readImpl() {
        // JSON Message
        //// session: = md5Hex of Client HWID
        Map<String, String> jsonMap = readJson();
        byte passLength = readByte();
        String pass = readString(passLength); // plain pass
        byte blowFishLength = readByte();
        blowFishKey = readBytes(blowFishLength);
        byte[] unk = readBytes(16); // unknown
        short unk2 = readShort(); // неизвестно, изменяется при каждом новом подключении, похоже на номер порта
        readShort(); // 0

        //System.out.println(ArrayUtils.bytesToHexString(content.copy(content.readerIndex(), content.readableBytes()).array()));
    }

    @Override
    public void runImpl() {
        getClient().setBlowFishKey(blowFishKey);
        getClient().setRandomKey(Rnd.nextInt());
        getClient().sendPacket(new AuthorizeResponse(sessionId));
    }
}
