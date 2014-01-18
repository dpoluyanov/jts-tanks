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
