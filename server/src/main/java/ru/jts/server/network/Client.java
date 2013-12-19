package ru.jts.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import ru.jts.common.network.udp.IClient;
import ru.jts.common.network.udp.IUDPServerPacketHandler;
import ru.jts.common.network.udp.ServerPacket;
import ru.jts.server.network.handler.RSAPacketHandler;

import java.net.InetSocketAddress;

/**
 * @author: Camelion
 * @date: 02.11.13/16:20
 * <p/>
 * Все действия с этим объектом должны быть потокобезопасными.
 */
public class Client implements IClient {

    private final InetSocketAddress address;
    private final InetSocketAddress serverAddress;
    private Channel channel;
    private IUDPServerPacketHandler<Client> packetHandler;

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
}
