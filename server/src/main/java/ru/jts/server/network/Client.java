package ru.jts.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import ru.jts.common.math.Rnd;
import ru.jts.common.network.udp.IClient;
import ru.jts.common.network.udp.IUDPServerPacketHandler;
import ru.jts.common.network.udp.ServerPacket;
import ru.jts.server.network.handler.RSAPacketHandler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @author: Camelion
 * @date: 02.11.13/16:20
 * <p/>
 */
public class Client implements IClient {

    private Channel channel;
    private IUDPServerPacketHandler<Client> packetHandler;
    private byte[] blowFishKey;
    private int randomKey;
    private String token2;
    private InetSocketAddress myAddress;

    public Client(InetSocketAddress myAddress, Channel channel) {
        this.channel = channel;
        this.packetHandler = RSAPacketHandler.getInstance();
        this.myAddress = myAddress;
    }

    public IUDPServerPacketHandler<Client> getPacketHandler() {
        return packetHandler;
    }

    public void sendPacket(ServerPacket<Client> packet) {
        packet.setClient(this);
        packet.write();

        ByteBuf buf = packet.getContent();
        buf = getPacketHandler().encrypt(buf);

        channel.writeAndFlush(new DatagramPacket(buf, myAddress));
    }

    public void setBlowFishKey(byte[] blowFishKey) {
        this.blowFishKey = blowFishKey;
    }

    public byte[] getBlowFishKey() {
        return blowFishKey;
    }

    public InetSocketAddress getServerAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    public int getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(int randomKey) {
        this.randomKey = randomKey;
    }

    public String generateToken2() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 38; i++) {
            token.append(Rnd.nextDigest());
        }

        return token2 = "1234567" + ":" + "1234567890123456789" + ":" + token;
    }
}
