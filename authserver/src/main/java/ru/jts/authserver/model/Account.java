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

package ru.jts.authserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author : Camelion
 * @date : 16.08.12  18:58
 */
@Entity
@Table(name = "accounts")
public class Account implements Serializable {
	@Id
	@Column(name = "login", length = 16)
	private String login;

	@Column(name = "password")
	private String password_hash;

	@Column(name = "last_server")
	private int lastServer;

	@Column(name = "last_ip")
	private String lastIP;

	// Restore account from DB
	public Account() {
		lastIP = "0.0.0.0";
	}

	// Create new account
	public Account(String login, String password_hash) {
		this();
		this.login = login;
		this.password_hash = password_hash;
	}

	public String getLogin() {
		return login;
	}

	public String getPasswordHash() {
		return password_hash;
	}

	public void setLastServer(int lastServer) {
		this.lastServer = lastServer;
	}

	public int getLastServer() {
		return lastServer;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}

	public String getLastIP() {
		return lastIP;
	}
}
