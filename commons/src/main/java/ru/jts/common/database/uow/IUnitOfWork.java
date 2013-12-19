package ru.jts.common.database.uow;

import com.mysema.query.jpa.impl.JPAQuery;

import java.io.Closeable;
import java.util.List;

public interface IUnitOfWork extends Closeable {
    <T> T get(Class<T> clazz, Object id);

    /**
     * save or update
     *
     * @param entity - добавляемый\обновляемый объект
     */
    void update(Object entity);

    /**
     * save
     *
     * @param entity - добавляемый объект
     */
    void save(Object entity);

    /**
     * delete
     *
     * @param entity - удаляемый объект
     */
    void delete(Object entity);

    <T> List<T> list(Class<T> clazz);

    JPAQuery query();

    ITransaction beginTransaction();

    void close();
}
