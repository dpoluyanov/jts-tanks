package ru.jts.common.network.udp;

import io.netty.buffer.ByteBuf;

/**
 * @author: Camelion
 * @date: 01.11.13/23:14
 */
public interface IUDPServerPacketHandler<T extends IClient> {
    /**
     * * Обрабатывает буффер, формирует из него пакет
     *
     * @param buf - буффер с данными
     * @return пакет, готовый для обработки, либо null
     */
    ClientPacket<T> handlePacket(ByteBuf buf);

    ByteBuf encrypt(ByteBuf buf);
}
