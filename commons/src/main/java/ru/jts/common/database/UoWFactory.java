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
