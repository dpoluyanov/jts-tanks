/*
 * Copyright 2012 jts
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

package ru.jts.server.database;

import ru.jts.common.database.UoWFactory;
import ru.jts.common.database.uow.ITransaction;
import ru.jts.common.database.uow.IUnitOfWork;
import ru.jts.common.log.Log;
import ru.jts.server.model.Account;

/**
 * @author : Camelion
 * @date : 16.08.12  19:47
 */
public class AccountsDAO {
    private static final String LOG_TAG = "AccountsDAO.java";
    private static AccountsDAO ourInstance = new AccountsDAO();

    public static AccountsDAO getInstance() {
        return ourInstance;
    }

    private AccountsDAO() {
    }

    public Account restoreByLogin(String login) {
        Account account = null;
        try (IUnitOfWork uow = UoWFactory.getInstance().createUoW()) {
            account = uow.get(Account.class, login);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Exception in restoreByLogin() for account '" + login + "'", e);
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
                Log.w(LOG_TAG, "Exception in saveAccount() for account '" + account.getLogin() + "'", e);
                transaction.rollbackIfActive();
            }
        }
    }
}
