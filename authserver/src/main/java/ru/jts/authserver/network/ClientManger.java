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

package ru.jts.authserver.network;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: Camelion
 * @date: 02.11.13/16:21
 */
public class ClientManger {
	private Map<SocketAddress, Client> clients;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock writeLock = lock.writeLock();

	private static ClientManger ourInstance = new ClientManger();

	public static ClientManger getInstance() {
		return ourInstance;
	}

	private ClientManger() {
		clients = new ConcurrentHashMap<>();
	}

	public Client getClientCreateIfNeed(InetSocketAddress sender, Channel channel) {
		Client client;
		writeLock.lock();
		if (!clients.containsKey(sender)) {
			client = new Client(sender, channel);
			clients.put(sender, client);
		} else {
			return clients.get(sender);
		}
		writeLock.unlock();
		return client;
	}
}
