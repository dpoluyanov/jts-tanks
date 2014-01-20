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

package ru.jts.common.network.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ru.jts.authserver.network.Client;
import ru.jts.common.util.ArrayUtils;

import java.nio.ByteOrder;

/**
 * @author: Camelion
 * @date: 20.01.14/11:50
 */
public abstract class Auth2ClientServerPacket extends ServerPacket<Client> {

	@Override
	public void write() {
		super.write();
		makeHeader();
	}

	private void makeHeader() {
		ByteBuf tempContent = Unpooled.buffer().capacity(content.readableBytes() + 7).order(ByteOrder.LITTLE_ENDIAN);
		tempContent.writeShort(0x00);
		tempContent.writeByte(0xFF);
		tempContent.writeInt(content.readableBytes());
		tempContent.writeBytes(content);

		content.clear();
		content = tempContent;
		System.out.println(ArrayUtils.bytesToHexString(content.copy().array()));
	}
}
