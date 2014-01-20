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

import java.nio.ByteOrder;

/**
 * @author: Camelion
 * @date: 29.11.13/4:36
 */
/* package */ abstract class ServerPacket<T extends IClient> {

	private T client;

	protected ByteBuf content;

	public ServerPacket() {
		content = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
	}

	public void write() {
		writeImpl();
	}

	protected void writeByte(int value) {
		content.writeByte(value);
	}

	protected void writeShort(int value) {
		content.writeShort(value);
	}

	protected void writeInt(int value) {
		content.writeInt(value);
	}

	protected void writeBytes(byte... values) {
		content.writeBytes(values);
	}

	protected void writeBytes(int... values) {
		for (int b : values)
			content.writeByte(b);
	}

	protected void writeBytes(ByteBuf buf) {
		content.writeBytes(buf);
	}

	protected void writeString(String str) {
		content.writeByte(str.length());
		content.writeBytes(str.getBytes());
	}

	protected abstract void writeImpl();

	public void setClient(T client) {
		this.client = client;
	}

	protected T getClient() {
		return client;
	}

	public ByteBuf getContent() {
		return content;
	}
}
