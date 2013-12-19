package ru.jts.server.network.crypt;

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

    public PrivateKey getKeyPair() {
        return keyPair.getPrivate();
    }
}
