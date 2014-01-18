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

import ru.jts.authserver.network.Client;
import ru.jts.common.network.udp.ServerPacket;
import ru.jts.common.util.ArrayUtils;

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
