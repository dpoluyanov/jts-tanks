package ru.jts.common.database.uow;

import javax.persistence.EntityTransaction;

/**
 * @author: Camelion
 * @date: 22.10.13/20:51
 */
public class JPATransaction implements ITransaction {
    private final EntityTransaction entityTransaction;

    public JPATransaction(EntityTransaction entityTransaction) {
        this.entityTransaction = entityTransaction;
    }

    @Override
    public void commit() {
        entityTransaction.commit();
    }

    @Override

    public void rollbackIfActive() {
        if (entityTransaction.isActive()) {
            entityTransaction.rollback();
        }
    }
}
