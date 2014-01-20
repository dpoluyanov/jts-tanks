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
import ru.jts.common.network.ClientPacket;
import ru.jts.common.network.IClient;

/**
 * @author: Camelion
 * @date: 01.11.13/23:14
 */
public interface IUDPServerPacketHandler<T extends IClient> {
	/**
	 * * Обрабатывает буффер, формирует из него пакет
	 *
	 * @param buf - буффер с данными
	 * @return пакет, готовый для обработки, либо null
	 */
	ClientPacket<T> handlePacket(ByteBuf buf);

	ByteBuf encrypt(ByteBuf buf);
}
