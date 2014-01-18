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

package ru.jts.authserver.network.clientpackets;

import ru.jts.authserver.network.Client;
import ru.jts.authserver.network.serverpackets.AuthorizeResponse;
import ru.jts.common.math.Rnd;
import ru.jts.common.network.udp.ClientPacket;

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
		readShort();

		//System.out.println(ArrayUtils.bytesToHexString(content.copy(content.readerIndex(), content.readableBytes()).array()));
	}

	@Override
	public void runImpl() {
		getClient().setBlowFishKey(blowFishKey);
		getClient().setRandomKey(Rnd.nextInt());

		getClient().sendPacket(new AuthorizeResponse(sessionId));
		//getClient().sendPacket(new InvalidPassword(sessionId));
	}
}
