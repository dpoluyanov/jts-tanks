package ru.jts.server.network.crypt;

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

    public byte[] encrypt(byte[] data, byte key[]) {
        byte[] output = null;
        try {
            byte additional = (byte) (8 - (data.length) % 8);
            byte[] addBytes = new byte[additional - 1];
            random.nextBytes(addBytes);
            ByteBuf buf = Unpooled.buffer(data.length + additional).order(ByteOrder.LITTLE_ENDIAN);
            buf.writeBytes(data, 0, data.length - 4);
            buf.writeBytes(addBytes);
            buf.writeBytes(data, data.length - 4, 4);
            buf.writeByte(additional);

            output = buf.array();
            byte[] d = buf.copy().array();

            Cipher rsa = Cipher.getInstance("Blowfish/ECB/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
            rsa.init(Cipher.ENCRYPT_MODE, keySpec);
            for (int i = 0; i < output.length; i += BLOCK_SIZE) {
                if (i > 0) {
                    for (int j = i; j < i + BLOCK_SIZE; j++) {
                        output[j] ^= d[j - BLOCK_SIZE];
                    }
                }
                rsa.doFinal(output, i, BLOCK_SIZE, output, i);
            }
        } catch (NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | ShortBufferException | NoSuchProviderException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return output;
    }
}
