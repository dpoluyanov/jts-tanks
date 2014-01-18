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

package ru.jts.authserver.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jts.authserver.model.Account;
import ru.jts.common.database.UoWFactory;
import ru.jts.common.database.uow.ITransaction;
import ru.jts.common.database.uow.IUnitOfWork;

/**
 * @author : Camelion
 * @date : 16.08.12  19:47
 */
public class AccountsDAO {
	private static final Logger log = LoggerFactory.getLogger(AccountsDAO.class);
	private static AccountsDAO ourInstance = new AccountsDAO();

	private AccountsDAO() {
	}

	public static AccountsDAO getInstance() {
		return ourInstance;
	}

	public Account restoreByLogin(String login) {
		Account account = null;
		try (IUnitOfWork uow = UoWFactory.getInstance().createUoW()) {
			account = uow.get(Account.class, login);
		} catch (Exception e) {
			log.warn("Exception in restoreByLogin() for account '" + login + "'", e);
		}
		return account;
	}

	public void saveAccount(Account account) {
		try (IUnitOfWork uow = UoWFactory.getInstance().createUoW()) {
			ITransaction transaction = uow.beginTransaction();
			try {
				uow.update(account);
				transaction.commit();
			} catch (Exception e) {
				log.warn("Exception in saveAccount() for account '" + account.getLogin() + "'", e);
				transaction.rollbackIfActive();
			}
		}
	}
}
