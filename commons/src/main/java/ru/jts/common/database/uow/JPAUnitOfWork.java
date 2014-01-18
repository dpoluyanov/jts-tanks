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
