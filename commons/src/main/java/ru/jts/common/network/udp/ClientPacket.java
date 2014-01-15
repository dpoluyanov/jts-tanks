package ru.jts.common.network.udp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import ru.jts.common.util.ArrayUtils;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Camelion
 * @date: 01.11.13/23:18
 */
public abstract class ClientPacket<T extends IClient> implements Runnable {
    private static final Pattern bracketsPattern = Pattern.compile("(\\{[^}]*\\})");

    protected ByteBuf content;
    private T client;

    public void setContent(ByteBuf content) {
        this.content = content;
    }

    public abstract void readImpl();
    public abstract void runImpl();

    @Override
    public void run() {
        readImpl();
        runImpl();
    }

    protected Map<String, String> readJson() {
        short size = content.readUnsignedByte();
        byte[] json = new byte[size];
        content.readBytes(json);

        ObjectMapper om = new ObjectMapper();
        try {
            Map<String, String> mapObject = om.readValue(json, new TypeReference<Map<String,String>>(){});

            return mapObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void skipBytes(int count) {
        content.skipBytes(count);
    }

    protected byte readByte() {
        return content.readByte();
    }

    protected short readShort() {
        return content.readShort();
    }

    protected String readString(int bytesCount) {
        byte b[] = new byte[bytesCount];
        content.readBytes(b);
        return new String(b);
    }

    protected byte[] readBytes(int count) {
        byte b[] = new byte[count];
        content.readBytes(b);
        return b;
    }

    public void setClient(T client) {
        this.client = client;
    }

    public T getClient() {
        return client;
    }
}
