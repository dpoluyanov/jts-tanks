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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Random;

/**
 * @author: Camelion
 * @date: 29.11.13/3:32
 */
public class CryptEngine {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static final int BLOCK_SIZE = 8;
	public static final int ZERO_TRAILING_MODE = 0;
	public static final int RANDOM_BYTES_MODE = 1;
	private static CryptEngine ourInstance = new CryptEngine();
	private Random random;

	public static CryptEngine getInstance() {
		return ourInstance;
	}

	private CryptEngine() {
		random = new Random(System.currentTimeMillis());
	}

	public byte[] decrypt(byte[] data, byte[] key) {
		try {
			Cipher rsa = Cipher.getInstance("Blowfish/ECB/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
			SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
			rsa.init(Cipher.DECRYPT_MODE, keySpec);
			for (int i = 0; i < data.length; i += BLOCK_SIZE) {
				rsa.doFinal(data, i, BLOCK_SIZE, data, i);
				if (i > 0) {
					for (int j = i; j < i + BLOCK_SIZE; j++) {
						data[j] ^= data[j - BLOCK_SIZE];
					}
				}
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | ShortBufferException e) {
			e.printStackTrace();
		}

		return data;
	}

	public byte[] encrypt(byte[] data, byte key[], int padding) {
		byte[] output = null;
		try {
			if (padding == ZERO_TRAILING_MODE) {
				data = zeroPadding(data);
			} else if (padding == RANDOM_BYTES_MODE) {
				data = randomBytesPadding(data);
			}

			byte[] original = new byte[data.length];
			System.arraycopy(data, 0, original, 0, data.length);
			output = data;

			Cipher rsa = Cipher.getInstance("Blowfish/ECB/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
			SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
			rsa.init(Cipher.ENCRYPT_MODE, keySpec);
			for (int i = 0; i < output.length; i += BLOCK_SIZE) {
				if (i > 0) {
					for (int j = i; j < i + BLOCK_SIZE; j++) {
						output[j] ^= original[j - BLOCK_SIZE];
					}
				}
				rsa.doFinal(output, i, BLOCK_SIZE, output, i);
			}
		} catch (NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | ShortBufferException | NoSuchProviderException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return output;
	}

	private byte[] randomBytesPadding(byte[] data) {
		byte additional = (byte) (8 - (data.length) % 8);
		byte[] addBytes = new byte[additional - 1];
		random.nextBytes(addBytes);
		ByteBuf buf = Unpooled.buffer(data.length + additional).order(ByteOrder.LITTLE_ENDIAN);
		buf.writeBytes(data, 0, data.length - 4);
		buf.writeBytes(addBytes);
		buf.writeBytes(data, data.length - 4, 4);
		buf.writeByte(additional);
		return buf.array();
	}

	private byte[] zeroPadding(byte[] data) {
		int additional = (8 - (data.length) % 8) % 8;
		byte[] out = data;
		if (additional > 0) {
			out = new byte[data.length + additional];
			System.arraycopy(data, 0, out, 0, data.length);
		}
		return out;
	}
}
