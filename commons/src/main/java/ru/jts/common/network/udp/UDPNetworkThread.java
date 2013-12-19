package ru.jts.common.network.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import ru.jts.common.network.NetworkConfig;

/**
 * @author: Camelion
 * @date: 01.11.13/22:25
 */
public class UDPNetworkThread {
    private final NetworkConfig networkConfig;
    private final ChannelHandler channelHandler;

    public UDPNetworkThread(NetworkConfig networkConfig, ChannelHandler channelHandler) {
        this.networkConfig = networkConfig;
        this.channelHandler = channelHandler;
    }

    public void startAsServer() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(channelHandler);

            b.bind(networkConfig.getSocketAddress()).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
