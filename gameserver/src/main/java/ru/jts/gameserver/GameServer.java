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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jts.common.configuration.Config;
import ru.jts.common.database.UoWFactory;
import ru.jts.common.network.NetworkThread;
import ru.jts.common.threading.ThreadPoolManager;
import ru.jts.gameserver.network.handler.Game2AuthChannelHandler;
import ru.jts.gameserver.network.handler.Game2ClientChannelHandler;

/**
 * @author : Camelion
 * @date : 20.01.14  15:52
 */
public class GameServer {
	private static final Logger log = LoggerFactory.getLogger(GameServer.class);

	static {
		Config.load("config/developers.properties");
		Config.load("config/server.properties");
		Config.load("config/network.properties");
		Config.load("config/thread_pool_manager.properties");
	}

	public static void main(String[] args) {
		ThreadPoolManager.getInstance().init(Config.getInt("thread_pool_manager.scheduled_thread_pool_size"),
				Config.getInt("thread_pool_manager.executor_thread_pool_size"));
		log.info("ThreadPoolManager created.");

		UoWFactory.getInstance().init("PersistenceUnit");

		log.info("UoWFactory loaded.");


		startNetworkServer();
		startNetworkClient();
	}

	public static void startNetworkServer() {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.option(ChannelOption.SO_BROADCAST, true);
		bootstrap.channel(NioDatagramChannel.class).handler(new Game2ClientChannelHandler());

		String host = Config.getString("network.game_clients.address");
		int port = Config.getInt("network.game_clients.port");
		if (host.equals("*")) {
			bootstrap.localAddress(port);
		} else {
			bootstrap.localAddress(host, port);
		}

		NetworkThread clientsNetworkThread = new NetworkThread(bootstrap, true);

		clientsNetworkThread.start();

		log.info("Clients NetworkThread loaded on {}:{}", Config.getString("network.game_clients.address"),
				Config.getInt("network.game_clients.port"));
	}

	private static void startNetworkClient() {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.option(ChannelOption.SO_BROADCAST, true);
		bootstrap.channel(NioDatagramChannel.class).handler(new Game2AuthChannelHandler());

		String host = Config.getString("network.auth_server.address");
		int port = Config.getInt("network.auth_server.port");
		bootstrap.remoteAddress(host, port);

		NetworkThread clientsNetworkThread = new NetworkThread(bootstrap, false);

		clientsNetworkThread.start();

		log.info("Connect to auth on {}:{}", Config.getString("network.auth_server.address"),
				Config.getInt("network.auth_server.port"));
	}
}
