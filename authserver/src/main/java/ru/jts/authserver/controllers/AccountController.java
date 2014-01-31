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

package ru.jts.authserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jts.authserver.configuration.AuthServerProperty;
import ru.jts.authserver.database.AccountsDAO;
import ru.jts.authserver.model.Account;
import ru.jts.common.enums.State;

import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Grizly(Skype: r-grizly)
 * @since 1.02.2014
 */
public class AccountController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<String, Account>();
    private Lock lock = new ReentrantLock();


    private AccountController() {
        log.info("Loaded");
    }

    public static AccountController getInstance() {
        return Singleton.INSTANCE;
    }

    public void accountConnect(Account account) {
        accounts.put(account.getLogin(), account);
    }

    public boolean isAccountConnected(String login) {
        return accounts.containsKey(login);
    }

    public void accountDisconnect(String login) {
        if (isAccountConnected(login)) {
            accounts.remove(login);
        }
    }

    public Account getAccount(String account) {
        return accounts.get(account);
    }


    public State accountLogin(Map accountInfo, String password) {
        State state = State.INVALID;
        lock.lock();
        String temporary = (String) accountInfo.get("temporary");
        String auth_realm = (String) accountInfo.get("auth_realm");
        String game = (String) accountInfo.get("game");
        String session = (String) accountInfo.get("session");
        String login = (String) accountInfo.get("login");
        String auth_method = (String) accountInfo.get("auth_method");

         if (isAccountConnected(login)) {
            state = State.ALREADY_AUTHED;
            log.info("Access denied for account: " + login + ". Reason: already connected.");
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(password.getBytes());
                StringBuffer result = new StringBuffer();
                for (byte byt : md.digest()) {
                    result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
                }
                password = result.toString();
            } catch (Exception e) {
            }
            Account account = AccountsDAO.getInstance().restoreByLogin(login);
            if (account == null) {
                if (AuthServerProperty.getInstance().ACCOUNT_AUTO_CREATE) {
                    account = new Account(login,password);
                    if (AccountsDAO.getInstance().saveAccount(account)) {
                        state = authAccount(account);
                        log.info("Created new account: " + login);
                    } else {
                        state = State.ERROR_AUTH;
                        log.info("Error create new account: " + login);
                    }
                } else {
                    state = State.ERROR_AUTH;
                    log.info("Authorization failed to account: " + login);
                }
            } else {
                state = authAccount(account);
            }
        }
        lock.unlock();
        return state;
    }

    private State authAccount(Account account) {
        accountConnect(account);
        log.debug("Successful login to account: " + account);
        return State.AUTHED;
    }

    private static class Singleton {
        private static final AccountController INSTANCE = new AccountController();
    }
}
