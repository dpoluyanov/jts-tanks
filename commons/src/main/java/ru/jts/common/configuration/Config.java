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

package ru.jts.common.configuration;

import ru.jts.common.GlobalConstans;
import ru.jts.common.log.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author : Camelion
 * @date : 10.08.12  10:28
 */
public class Config {
    private static final Map<String, String> propertiesMap = new HashMap<>();
    private static final String LOG_TAG = "Config.java";

    public static void load(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Log.w(LOG_TAG, "File {} don't exist! ", filePath);
            return;
        }

        if (file.isDirectory())
            loadFromDirectory(file);
        else
            loadFile(file);
    }

    private static void loadFromDirectory(File dir) {
        if (dir.listFiles() != null) {
            //noinspection ConstantConditions
            for (File file : dir.listFiles()) {
                if (file.isDirectory())
                    loadFromDirectory(file);
                else
                    loadFile(file);
            }
        }
    }

    private static void loadFile(File file) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            Log.w(LOG_TAG, "Exception in loadFile()", e);
            return;
        }
        for (String property : properties.stringPropertyNames()) {
            //noinspection PointlessBooleanExpression,ConstantConditions
            if (GlobalConstans.DEBUG && propertiesMap.containsKey(property))
                Log.d(LOG_TAG, " duplicate property {} ", property);
            propertiesMap.put(property, properties.getProperty(property));
        }

        if (GlobalConstans.DEBUG)
            Log.d(LOG_TAG, "loaded file {}", file.getName());
    }

    public static String getString(String key) {
        String ret = propertiesMap.get(key);
        return ret;
    }

    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public static boolean getBool(String key) {
        return Boolean.parseBoolean(getString(key));
    }
}
