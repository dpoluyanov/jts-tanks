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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jts.common.configuration.Config;
import ru.jts.common.database.UoWFactory;
import ru.jts.common.network.NetworkConfig;
import ru.jts.common.network.udp.UDPNetworkThread;
import ru.jts.common.threading.ThreadPoolManager;
import ru.jts.gameserver.network.handler.UDPServerHandler;

/**
 * @author : Camelion
 * @date : 20.01.14  15:52
 */
public class GameServer {
	private static final Logger log = LoggerFactory.getLogger(GameServer.class);

	static {
		Config.load("config/developers.properties");
		Config.load("config/authserver.properties");
		Config.load("config/network.properties");
		Config.load("config/thread_pool_manager.properties");
	}

	public static void main(String[] args) {
		ThreadPoolManager.getInstance().init(Config.getInt("thread_pool_manager.scheduled_thread_pool_size"),
				Config.getInt("thread_pool_manager.executor_thread_pool_size"));
		log.info("ThreadPoolManager created.");

		UoWFactory.getInstance().init("PersistenceUnit");

		log.info("UoWFactory loaded.");


		startNetworkServers();
	}

	public static void startNetworkServers() {
		NetworkConfig networkConfig = new NetworkConfig(Config.getString("network.game_clients.address"),
				Config.getInt("network.game_clients.port"), Config.getInt("network.game_clients.thread_count"));

		UDPNetworkThread networkThread = new UDPNetworkThread(networkConfig, new UDPServerHandler());

		networkThread.start();

		log.info("Clients NetworkThread loaded on {}:{}", Config.getString("network.game_clients.address"),
				Config.getInt("network.game_clients.port"));
	}
}
