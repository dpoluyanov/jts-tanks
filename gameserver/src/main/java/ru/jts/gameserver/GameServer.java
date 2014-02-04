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

package ru.jts.gameserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jts.common.database.UoWFactory;
import ru.jts.common.info.PrintInfo;
import ru.jts.common.network.NetworkThread;
import ru.jts.common.threading.ThreadPoolManager;
import ru.jts.gameserver.configuration.GameServerProperty;
import ru.jts.gameserver.network.handler.Game2AuthChannelInitializer;
import ru.jts.gameserver.network.handler.Game2ClientChannelHandler;

/**
 * @author : Camelion, Grizly(Skype: r-grizly)
 * @date : 20.01.14  15:52
 * @last : 31.01.2014
 */

public class GameServer {
	private static final Logger log = LoggerFactory.getLogger(GameServer.class);


	public static void main(String[] args) {
		PrintInfo.getInstance().printSection("Properties");
		GameServerProperty.getInstance();
		PrintInfo.getInstance().printSection("ThreadPoolManager");
		ThreadPoolManager.getInstance().init(GameServerProperty.getInstance().GAME_SCHEDULED_THREAD_POOL_SIZE,
				GameServerProperty.getInstance().GAME_EXECUTOR_THREAD_POOL_SIZE);
		log.info("ThreadPoolManager created.");

		PrintInfo.getInstance().printSection("UoWFactory");
		UoWFactory.getInstance().init("PersistenceUnit");
		log.info("UoWFactory loaded.");

		PrintInfo.getInstance().printSection("Load information");
		PrintInfo.getInstance().printLoadInfos();

		PrintInfo.getInstance().printSection("Network");
		startNetworkServer();
		startNetworkClient();
	}

	public static void startNetworkServer() {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.option(ChannelOption.SO_BROADCAST, true);
		bootstrap.channel(NioDatagramChannel.class).handler(new Game2ClientChannelHandler());

		String host = GameServerProperty.getInstance().GAME_CLIENT_HOST;
		int port = GameServerProperty.getInstance().GAME_CLIENT_PORT;
		if (host.equals("*")) {
			bootstrap.localAddress(port);
		} else {
			bootstrap.localAddress(host, port);
		}

		NetworkThread clientsNetworkThread = new NetworkThread(bootstrap, true);

		clientsNetworkThread.start();

		log.info("Clients NetworkThread loaded on {}:{}", host, port);
	}

	private static void startNetworkClient() {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.channel(NioSocketChannel.class).handler(new Game2AuthChannelInitializer());

		String host = GameServerProperty.getInstance().AUTH_CLIENT_HOST;
		int port = GameServerProperty.getInstance().AUTH_CLIENT_PORT;
		bootstrap.remoteAddress(host, port);

		NetworkThread clientsNetworkThread = new NetworkThread(bootstrap, false);

		clientsNetworkThread.start();

		log.info("Connect to auth on {}:{}", host, port);
	}
}
