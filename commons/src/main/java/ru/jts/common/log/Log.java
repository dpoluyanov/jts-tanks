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

package ru.jts.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : Camelion
 * @date : 12.08.12  23:34
 */
public class Log {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger("");
    }

    public static void i(Object obj) {
        i("{}", obj);
    }

    public static void i(String msg) {
        logger.info(msg);
    }

    public static void i(String tag, String msg) {
        logger.info(concat(tag, msg));
    }

    public static void i(String msg, Object param1) {
        logger.info(msg, param1);
    }

    public static void i(String tag, String msg, Object param1) {
        logger.info(concat(tag, msg), param1);
    }

    public static void i(String tag, String msg, Object param1, Object param2) {
        logger.info(concat(tag, msg), param1, param2);
    }

    public static void w(String msg) {
        logger.warn(msg);
    }

    public static void w(String tag, String msg) {
        logger.warn(concat(tag, msg));
    }

    public static void w(String msg, Throwable throwable) {
        logger.warn(msg, throwable);
    }

    public static void w(String tag, String msg, Throwable e) {
        logger.warn(concat(tag, msg), e);
    }

    public static void w(String tag, String msg, Object param1) {
        logger.warn(concat(tag, msg), param1);
    }

    public static void w(String tag, String msg, Object param1, Object param2) {
        logger.warn(concat(tag, msg), param1, param2);
    }

    public static void d(String msg) {
        logger.debug(msg);
    }

    public static void d(String msg, Throwable e) {
        logger.debug(msg, e);
    }

    public static void d(String tag, String msg) {
        logger.debug(concat(tag, msg));
    }

    public static void d(String tag, String msg, Object param1) {
        logger.debug(concat(tag, msg), param1);
    }

    public static void d(String tag, String msg, Object param1, Object param2) {
        logger.debug(concat(tag, msg), param1, param2);
    }

    public static void e(String tag, String msg, Object param1) {
        logger.error(concat(tag, msg), param1);
    }

    public static void e(String msg) {
        logger.error(msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        logger.error(concat(tag, msg), e);
    }

    private static String concat(String tag, String msg) {
        return tag + " : " + msg;
    }
}
