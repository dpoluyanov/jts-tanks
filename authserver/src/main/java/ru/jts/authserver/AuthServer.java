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

package ru.jts.authserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jts.common.info.*;
import ru.jts.authserver.network.handler.AuthClientsChannelHandler;
import ru.jts.authserver.network.handler.GameServersChannelHandler;
import ru.jts.common.configuration.Config;
import ru.jts.common.database.UoWFactory;
import ru.jts.common.network.NetworkThread;
import ru.jts.common.threading.ThreadPoolManager;

/**
 * @author : Camelion
 * @date : 10.08.12  1:07
 */
public class AuthServer {
	private static final Logger log = LoggerFactory.getLogger(AuthServer.class);

	static {
        PrintInfo.getInstance().printSection("Properties");
		Config.load("config/developers.properties");
		Config.load("config/authserver.properties");
		Config.load("config/network.properties");
		Config.load("config/thread_pool_manager.properties");
	}

	public static void main(String[] args) {
		ThreadPoolManager.getInstance().init(Config.getInt("thread_pool_manager.scheduled_thread_pool_size"),
				Config.getInt("thread_pool_manager.executor_thread_pool_size"));
        PrintInfo.getInstance().printSection("ThreadPoolManager");
		log.info("ThreadPoolManager created.");

        PrintInfo.getInstance().printSection("UoWFactory");
		UoWFactory.getInstance().init("PersistenceUnit");
		log.info("UoWFactory loaded.");

        PrintInfo.getInstance().printSection("Load information");
        PrintInfo.getInstance().printLoadInfos();

        PrintInfo.getInstance().printSection("Network");
        startNetworkServer();
	}

	public static void startNetworkServer() {
		// For Game Clients
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.option(ChannelOption.SO_BROADCAST, true);
		bootstrap.channel(NioDatagramChannel.class).handler(new AuthClientsChannelHandler());

		String host = Config.getString("network.auth_clients.address");
		int port = Config.getInt("network.auth_clients.port");
		if (host.equals("*")) {
			bootstrap.localAddress(port);
		} else {
			bootstrap.localAddress(host, port);
		}

		NetworkThread clientsNetworkThread = new NetworkThread(bootstrap, true);

		clientsNetworkThread.start();

		log.info("Clients NetworkThread loaded on {}:{}", Config.getString("network.auth_clients.address"),
				Config.getInt("network.auth_clients.port"));

		// For Game Servers
		bootstrap = new Bootstrap();
		bootstrap.channel(NioServerSocketChannel.class).handler(new GameServersChannelHandler());
		host = Config.getString("network.auth_clients.address");
		if (host.equals("*")) {
			bootstrap.localAddress(port);
		} else {
			bootstrap.localAddress(host, port);
		}

		NetworkThread serversNetworkThread = new NetworkThread(bootstrap, true);
		serversNetworkThread.start();

		log.info("Servers NetworkThread loaded on {}:{}", Config.getString("network.auth_clients.address"),
				Config.getInt("network.auth_clients.port"));
	}
}
