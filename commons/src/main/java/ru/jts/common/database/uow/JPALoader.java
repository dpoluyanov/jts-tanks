package ru.jts.common.database.uow;

import java.util.List;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;

/**
 * @author: Camelion
 * @date: 05.11.13/21:45
 */
public class JPALoader<T> {

    private final Class<T> entityClass;

    public JPALoader(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public List<T> getList(IUnitOfWork uow) {
        return uow.query().from($(getAlias())).list($(getAlias()));
    }

    protected T getAlias() {
        return alias(entityClass, "entity");
    }
}
