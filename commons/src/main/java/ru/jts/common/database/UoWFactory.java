package ru.jts.common.database;

import ru.jts.common.database.uow.IUnitOfWork;
import ru.jts.common.database.uow.JPAUnitOfWork;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UoWFactory {
    private EntityManagerFactory entityManagerFactory;

    private static UoWFactory ourInstance = new UoWFactory();

    public static UoWFactory getInstance() {
        return ourInstance;
    }

    private UoWFactory() {
        init();
    }

    private void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnit");
    }

    public IUnitOfWork createUoW() {
        return new JPAUnitOfWork(entityManagerFactory.createEntityManager());
    }
}
