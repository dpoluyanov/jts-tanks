/*
 * Copyright 2014 jts
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

package ru.jts.authserver.network.handler.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jts.authserver.network.Client;
import ru.jts.authserver.network.clientpackets.CM_AUTH_BY_PASS;
import ru.jts.authserver.network.crypt.RSAEngine;
import ru.jts.common.network.ClientPacket;
import ru.jts.common.network.IPacketHandler;
import ru.jts.common.util.ArrayUtils;

import java.nio.ByteOrder;

/**
 * @author : Camelion
 * @date : 20.08.12  14:55
 */
public class Client2AuthPacketHandler implements IPacketHandler<Client> {
	private static final Logger log = LoggerFactory.getLogger(Client2AuthPacketHandler.class);

	private static Client2AuthPacketHandler ourInstance = new Client2AuthPacketHandler();

	public static Client2AuthPacketHandler getInstance() {
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
				packet = new CM_AUTH_BY_PASS(sessionId);
				break;
			//case 0x02:
			//    packet = new AuthorizeBySession();
			//    break;
			default:
				log.warn("Unknown opcode {}", Integer.toHexString(opcode));
				log.warn(ArrayUtils.bytesToHexString(packetBuf.copy(packetBuf.readerIndex(), packetBuf.readableBytes()).array()));
				log.warn(new String(packetBuf.copy(packetBuf.readerIndex(), packetBuf.readableBytes()).array()));
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

	@Override
	public ByteBuf decrypt(ByteBuf buf) {
		return buf;
	}

	private ByteBuf decryptBuffer(ByteBuf buf) {
		byte[] data = RSAEngine.getInstance().decrypt(buf.array(), 0, buf.array().length);
		return Unpooled.copiedBuffer(data).order(ByteOrder.LITTLE_ENDIAN);
	}
}
