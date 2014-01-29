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

package ru.jts.common.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Grizly(Skype: r-grizly)
 * @since 29.01.2014
 */
public class PrintInfo {
    private static final PrintInfo instance = new PrintInfo();
    private static final Logger log = LoggerFactory.getLogger(PrintInfo.class);
    private static final long startTime = System.currentTimeMillis();

    private PrintInfo() {
    }

    public static PrintInfo getInstance() {
        return instance;
    }

    public void printSection(String s) {
        s = "-[ " + s + " ]";
        while (s.length() < 79) {
            s = "=" + s;
        }
        log.info(s);
    }

    private void print(String g, Object... a) {
        log.info(String.format(g, a));
    }

    public void printLoadInfos() {
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        print("Loaded in %d sec(%.3f min)", seconds, (seconds / 60F));
        print("Used %s, max %s.", MemoryInfo.getMemoryUsedMb(), MemoryInfo.getMemoryMaxMb());
        print("Used %.2f%% percent memory.", MemoryInfo.getMemoryUsagePercent());
    }
}

