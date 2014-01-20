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

package ru.jts.authserver.network.serverpackets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ru.jts.common.network.udp.Auth2ClientServerPacket;
import ru.jts.common.util.ArrayUtils;

/**
 * @author: Camelion
 * @date: 20.12.13/1:36
 */
public class AuthorizeResponse extends Auth2ClientServerPacket {
	public static final byte LOGIN_OK = 0x01;
	public static final byte LOGIN_REJECTED_INVALID_PASSWORD = 0x43;

	private final short sessionId;
	private final byte[] data;
	private final byte responseCode;

	public AuthorizeResponse(short sessionId, byte responseCode, byte[] data) {
		this.sessionId = sessionId;
		this.responseCode = responseCode;
		this.data = data;
	}

	public AuthorizeResponse(short sessionId, byte responseCode, String data) {
		this.sessionId = sessionId;
		this.responseCode = responseCode;
		this.data = makeDataFromString(data);
	}

	private byte[] makeDataFromString(String data) {
		ByteBuf buf = Unpooled.buffer().capacity(data.length() + 1);
		buf.writeByte(data.length());
		buf.writeBytes(data.getBytes());
		return buf.array();
	}

	@Override
	protected void writeImpl() {
		writeShort(sessionId);
		writeBytes(0x00, 0x00);
		writeByte(responseCode); // LOGIN_OK
		writeBytes(data);

		System.out.println(ArrayUtils.bytesToHexString(getClient().getBlowFishKey()));
	}
}
