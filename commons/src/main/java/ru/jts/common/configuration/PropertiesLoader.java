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

package ru.jts.common.configuration;

import java.util.Properties;

/**
 * @author Grizly(Skype: r-grizly)
 * @since 31.01.2014
 */
public class PropertiesLoader {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final Properties PROPERTIES = new Properties();

    protected PropertiesLoader(String fname) {
        try {
            PROPERTIES.load(this.getClass().getClassLoader().getResourceAsStream(fname));
            log.info("Loaded properties from [" + fname + "]");
        } catch (Exception e) {
            log.error("Error load file: [" + fname + "] Application shutdown!", e);
            System.exit(1);
        }
    }

    public Boolean loadBoolean(String key) {
        Boolean value = Boolean.valueOf(PROPERTIES.getProperty(key));
        log.info("Load " + key + " = [" + value + "]");
        return value;
    }

    public Integer loadInteger(String key) {
        Integer value = Integer.valueOf(PROPERTIES.getProperty(key));
        log.info("Load " + key + " = [" + value + "]");
        return value;
    }

    public String loadString(String key) {
        String value = PROPERTIES.getProperty(key);
        log.info("Load " + key + " = [" + value + "]");
        return value;
    }
}
