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

package ru.jts.common.threading;

import java.util.concurrent.*;

/**
 * @author : Camelion
 * @date : 15.08.12  14:04
 */
public class ThreadPoolManager {
    private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;
    private boolean initied = false;

    private ScheduledThreadPoolExecutor scheduledExecutor;
    private ThreadPoolExecutor executor;

    private static ThreadPoolManager ourInstance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return ourInstance;
    }

    private ThreadPoolManager() {
    }

    public void init(int scheduledThreadPoolSize, int executorThreadPoolSize) {
        if (!initied) {
            scheduledExecutor = new ScheduledThreadPoolExecutor(scheduledThreadPoolSize);
            executor = new ThreadPoolExecutor(executorThreadPoolSize, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            initied = true;
        }
    }

    private static long validate(long delay) {
        return Math.max(0, Math.min(MAX_DELAY, delay));
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        if (!initied)
            throw new IllegalStateException("ThreadPoolManager schedule() called, but manager is not initialized");
        return scheduledExecutor.schedule(r, validate(delay), TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initial, long delay) {
        if (!initied)
            throw new IllegalStateException("ThreadPoolManager scheduleAtFixedRate() called, but manager is not initialized");
        return scheduledExecutor.scheduleAtFixedRate(r, validate(initial), validate(delay), TimeUnit.MILLISECONDS);
    }

    public void execute(Runnable r) {
        if (!initied)
            throw new IllegalStateException("ThreadPoolManager execute() called, but manager is not initialized");
        executor.execute(r);
    }
}
