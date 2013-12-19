package ru.jts.common.database.uow;

import com.mysema.query.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class JPAUnitOfWork implements IUnitOfWork {
    private final EntityManager entityManager;

    public JPAUnitOfWork(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public <T> T get(Class<T> clazz, Object id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public void update(Object entity) {
        entityManager.merge(entity);
    }

    @Override
    public void save(Object entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(Object entity) {
        entityManager.remove(entity);
    }

    @Override
    public <T> List<T> list(Class<T> clazz) {
        return new JPALoader<>(clazz).getList(this);
    }

    @Override
    public JPAQuery query() {
        return new JPAQuery(entityManager);
    }

    @Override
    public ITransaction beginTransaction() {
        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        return new JPATransaction(et);
    }

    @Override
    public void close() {
        entityManager.close();
    }
}
