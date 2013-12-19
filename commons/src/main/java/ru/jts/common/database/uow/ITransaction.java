package ru.jts.common.database.uow;

/**
 * @author: Camelion
 * @date: 22.10.13/20:51
 */
public interface ITransaction {
    void commit();

    void rollbackIfActive();
}
