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
