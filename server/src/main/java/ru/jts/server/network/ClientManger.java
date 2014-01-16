package ru.jts.server.network;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: Camelion
 * @date: 02.11.13/16:21
 */
public class ClientManger {
    private Map<SocketAddress, Client> clients;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();

    private static ClientManger ourInstance = new ClientManger();

    public static ClientManger getInstance() {
        return ourInstance;
    }

    private ClientManger() {
        clients = new ConcurrentHashMap<>();
    }

    public Client getClientCreateIfNeed(InetSocketAddress sender, Channel channel) {
        Client client;
        writeLock.lock();
        if (!clients.containsKey(sender)) {
            client = new Client(sender, channel);
            clients.put(sender, client);
        } else {
            return clients.get(sender);
        }
        writeLock.unlock();
        return client;
    }
}
