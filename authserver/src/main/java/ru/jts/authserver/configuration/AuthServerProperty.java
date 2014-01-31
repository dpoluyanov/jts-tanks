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

package ru.jts.authserver.configuration;

import ru.jts.common.configuration.PropertiesLoader;

/**
 * @author Grizly(Skype: r-grizly)
 * @since 31.01.2014
 * @last 1.02.2014
 */
public class AuthServerProperty extends PropertiesLoader {

    public final String AUTH_CLIENT_HOST = loadString("auth.client.host");
    public final Integer AUTH_CLIENT_PORT = loadInteger("auth.client.port");
    public final Integer AUTH_SCHEDULED_THREAD_POOL_SIZE = loadInteger("auth.scheduled.thread.pool.size");
    public final Integer AUTH_EXECUTOR_THREAD_POOL_SIZE = loadInteger("auth.executor.thread.pool.size");
    public final Boolean ACCOUNT_AUTO_CREATE = loadBoolean("account.auto.create");

    private AuthServerProperty() {
        super("auth.properties");
    }

    public static AuthServerProperty getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static final AuthServerProperty INSTANCE = new AuthServerProperty();
    }
}
