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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author: Camelion
 * @date: 01.11.13/22:25
 */
public class NetworkThread extends Thread {
	private final Bootstrap b;
	private final boolean server;

	public NetworkThread(Bootstrap b, boolean server) {
		this.b = b;
		this.server = server;
	}

	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			b.group(group);
			if(server)
				b.bind().sync().channel().closeFuture().await();
			else
				b.connect().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
