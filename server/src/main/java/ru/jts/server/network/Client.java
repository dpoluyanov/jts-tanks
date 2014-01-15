package ru.jts.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import ru.jts.common.math.Rnd;
import ru.jts.common.network.udp.IClient;
import ru.jts.common.network.udp.IUDPServerPacketHandler;
import ru.jts.common.network.udp.ServerPacket;
import ru.jts.server.network.crypt.CryptEngine;
import ru.jts.server.network.handler.RSAPacketHandler;

import java.net.InetSocketAddress;

/**
 * @author: Camelion
 * @date: 02.11.13/16:20
 * <p/>
 */
public class Client implements IClient {

    private final InetSocketAddress address;
    private final InetSocketAddress serverAddress;
    private Channel channel;
    private IUDPServerPacketHandler<Client> packetHandler;
    private byte[] blowFishKey;
    private int randomKey;
    private String token2;

    public Client(InetSocketAddress serverAddress, InetSocketAddress address, Channel channel) {
        this.serverAddress = serverAddress;
        this.address = address;
        this.channel = channel;
        this.packetHandler = RSAPacketHandler.getInstance();
    }

    public IUDPServerPacketHandler<Client> getPacketHandler() {
        return packetHandler;
    }

    public void sendPacket(ServerPacket<Client> packet) {
        packet.setClient(this);
        packet.write();

        ByteBuf buf = packet.getContent();
        buf = getPacketHandler().encrypt(buf);

        channel.writeAndFlush(new DatagramPacket(buf, serverAddress));
    }

    public void setBlowFishKey(byte[] blowFishKey) {
        this.blowFishKey = blowFishKey;
    }

    private byte[] getBlowFishKey() {
        return blowFishKey;
    }

    public InetSocketAddress getServerAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    public byte[] encrypt(byte[] array) {
        if (getBlowFishKey() == null)
            throw new NullPointerException("Blowfish key is null");

        return CryptEngine.getInstance().encrypt(array, getBlowFishKey());
    }

    public int getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(int randomKey) {
        this.randomKey = randomKey;
    }

    public String generateToken2() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 39; i++) {
            token.append(Rnd.nextDigest());
        }

        return token2 = "1234567" + ":" + "1234567890123456789" + ":" + token;
    }
}
