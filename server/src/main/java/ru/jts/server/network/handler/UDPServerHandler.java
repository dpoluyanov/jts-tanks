package ru.jts.server.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import ru.jts.common.network.NetworkConfig;
import ru.jts.common.network.udp.ClientPacket;
import ru.jts.common.threading.ThreadPoolManager;
import ru.jts.server.network.Client;
import ru.jts.server.network.ClientManger;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

/**
 * @author: Camelion
 * @date: 01.11.13/22:35
 */
public class UDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        Client client = ClientManger.getInstance().getClientCreateIfNeed(packet.sender(), ctx.channel());

        ByteBuf buf = packet.content().order(ByteOrder.LITTLE_ENDIAN);

        ClientPacket<Client> clientPacket = client.getPacketHandler().handlePacket(buf);
        if (clientPacket != null) {
            clientPacket.setClient(client);
            ThreadPoolManager.getInstance().execute(clientPacket);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
