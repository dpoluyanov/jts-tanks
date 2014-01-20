/*
 * Copyright 2014 jts
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.jts.common.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import ru.jts.common.database.UoWFactory;
import ru.jts.common.database.uow.ITransaction;
import ru.jts.common.database.uow.IUnitOfWork;

/**
 * @author: Camelion
 * @date: 20.01.14/11:58
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