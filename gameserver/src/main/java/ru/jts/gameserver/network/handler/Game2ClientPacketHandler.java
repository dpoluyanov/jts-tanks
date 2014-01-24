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

package ru.jts.gameserver.network.handler;

import io.netty.buffer.ByteBuf;
import ru.jts.common.network.ClientPacket;
import ru.jts.common.network.IPacketHandler;
import ru.jts.gameserver.network.Client;

/**
 * @author: Camelion
 * @date: 20.01.14/16:11
 */
public class Game2ClientPacketHandler implements IPacketHandler<Client> {
	private static Game2ClientPacketHandler ourInstance = new Game2ClientPacketHandler();

	public static Game2ClientPacketHandler getInstance() {
		return ourInstance;
	}

	@Override
	public ClientPacket<Client> handlePacket(ByteBuf buf) {
		return null;
	}

	@Override
	public ByteBuf encrypt(ByteBuf buf) {
		return null;
	}

	@Override
	public ByteBuf decrypt(ByteBuf buf) {
		return null;
	}
}
