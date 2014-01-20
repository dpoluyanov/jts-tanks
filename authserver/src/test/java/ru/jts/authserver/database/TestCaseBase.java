package ru.jts.authserver.database;

import org.junit.Assert;
import org.junit.BeforeClass;
import ru.jts.common.database.UoWFactory;
import ru.jts.common.database.uow.ITransaction;
import ru.jts.common.database.uow.IUnitOfWork;

/**
 * @author: Camelion
 * @date: 18.12.13/2:05
 */
public class TestCaseBase extends Assert {

	@BeforeClass
	public static void testCaseBaseBeforeClass() {
		if (!UoWFactory.getInstance().isInitialized())
			UoWFactory.getInstance().init("TEST_PERSISTENCE");
	}

	public IUnitOfWork createUoW() {
		return UoWFactory.getInstance().createUoW();
	}

	public void save(Object object) {
		ITransaction tx = null;
		try (IUnitOfWork uow = createUoW()) {
			tx = uow.beginTransaction();
			uow.save(object);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollbackIfActive();
			throw e;
		}
	}

	public void update(Object object) {
		ITransaction tx = null;
		try (IUnitOfWork uow = createUoW()) {
			tx = uow.beginTransaction();
			uow.update(object);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollbackIfActive();
			throw e;
		}
	}

	public <T> T get(Class<T> clazz, Object id) {
		try (IUnitOfWork uow = createUoW()) {
			return uow.get(clazz, id);
		}
	}
}
