package ru.jts.common.network.udp;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteOrder;

/**
 * @author: Camelion
 * @date: 29.11.13/4:36
 */
public abstract class ServerPacket<T extends IClient> {

    private T client;

    protected ByteBuf content;

    public ServerPacket() {
        content = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
    }

    public void write() {
        try {
            before();
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeImpl();
    }

    /**
     * Вызываеся перед записью данных
     */
    protected void before() throws Exception {

    }

    protected void writeByte(int value) {
        content.writeByte(value);
    }

    protected void writeShort(int value) {
        content.writeShort(value);
    }

    protected void writeInt(int value) {
        content.writeInt(value);
    }

    protected void writeBytes(byte... values) {
        content.writeBytes(values);
    }

    protected void writeBytes(int... values) {
        for(int b : values)
            content.writeByte(b);
    }

    protected void writeBytes(ByteBuf buf) {
        content.writeBytes(buf);
    }



    protected abstract void writeImpl();

    public void setClient(T client) {
        this.client = client;
    }

    protected T getClient() {
        return client;
    }

    public ByteBuf getContent() {
        return content;
    }
}
