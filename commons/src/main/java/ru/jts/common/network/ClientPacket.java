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

package ru.jts.common.network;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Map;

/**
 * @author: Camelion
 * @date: 01.11.13/23:18
 */
public abstract class ClientPacket<T extends IClient> implements Runnable {

	protected ByteBuf content;
	private T client;

	public void setContent(ByteBuf content) {
		this.content = content;
	}

	public abstract void readImpl();

	public abstract void runImpl();

	@Override
	public void run() {
		readImpl();
		runImpl();
	}

	protected Map<String, String> readJson() {
		short size = content.readUnsignedByte();

		byte[] json = new byte[size];
		content.readBytes(json);

		ObjectMapper om = new ObjectMapper();
		try {
			Map<String, String> mapObject = om.readValue(json, new TypeReference<Map<String, String>>() {
			});

			return mapObject;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected byte readByte() {
		return content.readByte();
	}

	protected short readShort() {
		return content.readShort();
	}

	protected String readString(int bytesCount) {
		byte b[] = new byte[bytesCount];
		content.readBytes(b);
		return new String(b);
	}

	protected byte[] readBytes(int count) {
		byte b[] = new byte[count];
		content.readBytes(b);
		return b;
	}

	public T getClient() {
		return client;
	}

	public void setClient(T client) {
		this.client = client;
	}
}
