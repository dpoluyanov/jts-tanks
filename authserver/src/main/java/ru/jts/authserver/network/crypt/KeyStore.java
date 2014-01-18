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

package ru.jts.authserver.network.crypt;

import org.bouncycastle.openssl.PEMReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;

/**
 * @author: Camelion
 * @date: 02.11.13/14:06
 */
public class KeyStore {
	private KeyPair keyPair;
	private static KeyStore ourInstance = new KeyStore();

	public static KeyStore getInstance() {
		return ourInstance;
	}

	private KeyStore() {
		loadRSAPair();
	}

	private void loadRSAPair() {
		FileReader fileReader = null;
		PEMReader r = null;
		try {
			fileReader = new FileReader(new File("key.pem"));
			r = new PEMReader(fileReader);
			keyPair = (KeyPair) r.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (r != null)
					r.close();
				if (fileReader != null)
					fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}
}
