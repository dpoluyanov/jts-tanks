/*
 * Copyright 2012 jts
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.jts.server.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ru.jts.common.log.Log;
import ru.jts.common.network.udp.ClientPacket;
import ru.jts.common.network.udp.IUDPServerPacketHandler;
import ru.jts.common.network.udp.ServerPacket;
import ru.jts.common.util.ArrayUtils;
import ru.jts.server.network.Client;
import ru.jts.server.network.clientpackets.AuthorizeByPassword;
import ru.jts.server.network.clientpackets.AuthorizeBySession;
import ru.jts.server.network.crypt.RSAEngine;

import java.nio.ByteOrder;

/**
 * @author : Camelion
 * @date : 20.08.12  14:55
 */
public class RSAPacketHandler implements IUDPServerPacketHandler<Client> {
    private static final String LOG_TAG = "RSAPacketHandler.java";

    private static RSAPacketHandler ourInstance = new RSAPacketHandler();

    public static RSAPacketHandler getInstance() {
        return ourInstance;
    }

    @Override
    public ClientPacket<Client> handlePacket(ByteBuf buf) {
        byte b1 = buf.readByte(); // 1
        short unk1 = buf.readShort(); // 0
        short size = buf.readShort(); // data size

        short sessionId = buf.readShort(); // sessionKey
        byte b2 = buf.readByte(); // 1

        int unk2 = buf.readInt(); // 0
        byte b3 = buf.readByte(); // 8

        byte[] packetData = new byte[buf.readableBytes() - 4];
        short unk3 = buf.readShort(); // 00 02
        buf.readBytes(packetData);
        short unk4 = buf.readShort(); // 02 00
        buf.clear();

        ByteBuf packetBuf = Unpooled.copiedBuffer(packetData).order(ByteOrder.LITTLE_ENDIAN);
        packetBuf = decryptBuffer(packetBuf);

        int opcode = packetBuf.readUnsignedByte();

        ClientPacket<Client> packet = null;
        switch (opcode) {
            case 0x01:
                packet = new AuthorizeByPassword(sessionId);
                break;
            //case 0x02:
            //    packet = new AuthorizeBySession();
            //    break;
            default:
                Log.w(LOG_TAG, "Unknown opcode {}", Integer.toHexString(opcode));
                Log.w(LOG_TAG, ArrayUtils.bytesToHexString(packetBuf.copy(packetBuf.readerIndex(), packetBuf.readableBytes()).array()));
                Log.w(LOG_TAG, new String(packetBuf.copy(packetBuf.readerIndex(), packetBuf.readableBytes()).array()));
                break;
        }

        if (packet != null) {
            packet.setContent(packetBuf);
        }
        return packet;
    }

    @Override
    public ByteBuf encrypt(ByteBuf buf) {
        return buf;
    }

    private ByteBuf decryptBuffer(ByteBuf buf) {
        byte[] data = RSAEngine.getInstance().decrypt(buf.array(), 0, buf.array().length);
        return Unpooled.copiedBuffer(data).order(ByteOrder.LITTLE_ENDIAN);
    }
}
