package ru.jts.server.network.crypt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteOrder;
import java.security.*;

/**
 * @author: Camelion
 * @date: 31.10.13/20:22
 */
public class RSAEngine {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static RSAEngine ourInstance = new RSAEngine();

    public static RSAEngine getInstance() {
        return ourInstance;
    }

    private RSAEngine() {
    }

    public byte[] decrypt(byte[] data, int from, int length) {
        return decrypt0(data, from, length);
    }

    /**
     * @param data   - входной массив закриптованных данных
     * @param from
     * @param length
     * @return - массив расшифрованных данных
     */
    private byte[] decrypt0(byte[] data, int from, int length) {
        ByteBuf buf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
        PrivateKey privateKey = KeyStore.getInstance().getKeyPair();
        try {
            Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", BouncyCastleProvider.PROVIDER_NAME);
            rsa.init(Cipher.DECRYPT_MODE, privateKey);

            final int blockSize = rsa.getBlockSize();
            for (int i = from; i < length; i += blockSize) {
                if (i + blockSize > length) {
                    byte[] tempData = rsa.doFinal(data, i, length - i);
                    buf.writeBytes(tempData);
                } else {
                    byte[] tempData = rsa.doFinal(data, i, blockSize);
                    buf.writeBytes(tempData);
                }
            }
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                BadPaddingException |
                IllegalBlockSizeException |
                NoSuchProviderException e) {
            e.printStackTrace();
        }
        return buf.array();
    }
}