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
import ru.jts.authserver.configuration.AuthServerProperty;
import ru.jts.authserver.controllers.LoaderController;
import ru.jts.common.info.*;
import ru.jts.authserver.network.handler.AuthClientsChannelHandler;
import ru.jts.authserver.network.handler.GameServersChannelHandler;
import ru.jts.common.database.UoWFactory;
import ru.jts.common.network.NetworkThread;
import ru.jts.common.threading.ThreadPoolManager;

/**
 * @author : Camelion, Grizly(Skype: r-grizly)
 * @since  : 10.08.12  1:07
 * @last   : 31.01.2014
 */
public class AuthServer {
	private static final Logger log = LoggerFactory.getLogger(AuthServer.class);

	public static void main(String[] args) {
        PrintInfo.getInstance().printSection("Properties");
        AuthServerProperty.getInstance();
        PrintInfo.getInstance().printSection("ThreadPoolManager");
        ThreadPoolManager.getInstance().init(AuthServerProperty.getInstance().AUTH_SCHEDULED_THREAD_POOL_SIZE,
				                             AuthServerProperty.getInstance().AUTH_EXECUTOR_THREAD_POOL_SIZE);
		log.info("ThreadPoolManager created.");
        PrintInfo.getInstance().printSection("UoWFactory");
		UoWFactory.getInstance().init("PersistenceUnit");
		log.info("UoWFactory loaded.");

        PrintInfo.getInstance().printSection("Controllers");
        LoaderController.loadControllers();

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

		String host = AuthServerProperty.getInstance().AUTH_CLIENT_HOST;
		int port = AuthServerProperty.getInstance().AUTH_CLIENT_PORT;
		if (host.equals("*")) {
			bootstrap.localAddress(port);
		} else {
			bootstrap.localAddress(host, port);
		}

		NetworkThread clientsNetworkThread = new NetworkThread(bootstrap, true);

		clientsNetworkThread.start();

		log.info("Clients NetworkThread loaded on {}:{}", host, port);

        //TODO Переделаю этот бардак чутка позже
		// For Game Servers
		bootstrap = new Bootstrap();
		bootstrap.channel(NioServerSocketChannel.class).handler(new GameServersChannelHandler());
		if (host.equals("*")) {
			bootstrap.localAddress(port);
		} else {
			bootstrap.localAddress(host, port);
		}

		NetworkThread serversNetworkThread = new NetworkThread(bootstrap, true);
		serversNetworkThread.start();

		log.info("Servers NetworkThread loaded on {}:{}", host, port);
	}
}
