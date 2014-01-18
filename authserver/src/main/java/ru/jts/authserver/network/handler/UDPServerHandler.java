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

package ru.jts.authserver.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import ru.jts.authserver.network.Client;
import ru.jts.authserver.network.ClientManger;
import ru.jts.common.network.udp.ClientPacket;
import ru.jts.common.threading.ThreadPoolManager;

import java.nio.ByteOrder;

/**
 * @author: Camelion
 * @date: 01.11.13/22:35
 */
public class UDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		Client client = ClientManger.getInstance().getClientCreateIfNeed(packet.sender(), ctx.channel());

		ByteBuf buf = packet.content().order(ByteOrder.LITTLE_ENDIAN);

		ClientPacket<Client> clientPacket = client.getPacketHandler().handlePacket(buf);
		if (clientPacket != null) {
			clientPacket.setClient(client);
			ThreadPoolManager.getInstance().execute(clientPacket);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}
}
