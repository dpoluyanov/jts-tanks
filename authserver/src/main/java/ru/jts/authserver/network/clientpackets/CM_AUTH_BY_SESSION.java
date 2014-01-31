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
import ru.jts.common.network.ClientPacket;

import java.util.Map;

/**
 * @author: Camelion
 * @date: 12.11.13/14:06
 */
public class CM_AUTH_BY_SESSION extends ClientPacket<Client> {
	@Override
	public void readImpl() {
		Map<String, String> jsonMsg = readJson();

		byte _0x7D = readByte();
		byte unk = readByte(); // 0x00
		byte blowFishLength = readByte();
		byte[] blowFish = readBytes(blowFishLength);
		readBytes(16); // unknown
		short unk2 = readShort(); // неизвестно, изменяется при каждом новом подключении
		readShort(); // 0

		//System.out.println(ArrayUtils.bytesToHexString(content.copy(content.readerIndex(), content.readableBytes()).array()));
	}

	@Override
	public void runImpl() {

	}
}
