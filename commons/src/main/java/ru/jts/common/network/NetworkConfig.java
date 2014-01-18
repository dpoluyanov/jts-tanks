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

import java.net.InetSocketAddress;

/**
 * @author : Camelion
 * @date : 13.08.12  13:44
 */
public class NetworkConfig {
	private final int port;
	private final int threadCount;
	private final InetSocketAddress socketAddress;

	public NetworkConfig(String address, int port, int threadCount) {
		this.port = port;
		this.threadCount = threadCount;

		if (address.equals("*"))
			socketAddress = new InetSocketAddress(port);
		else
			socketAddress = new InetSocketAddress(address, port);
	}

	public InetSocketAddress getSocketAddress() {
		return socketAddress;
	}

	public int getThreadCount() {
		return threadCount;
	}
}
