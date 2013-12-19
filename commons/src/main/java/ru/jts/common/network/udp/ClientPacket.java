package ru.jts.common.network.udp;

import io.netty.buffer.ByteBuf;

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

    @Override
    public void run() {
        read();
    }

    private void read() {
        readImpl();
    }

    protected String readBrackets() {
        Matcher matcher = bracketsPattern.matcher(new String(content.array()));
        String s = "{}";
        if (matcher.find()) {
            s = matcher.group(1);
            skipBytes(s.getBytes().length - 1);
        }
        return s;
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
