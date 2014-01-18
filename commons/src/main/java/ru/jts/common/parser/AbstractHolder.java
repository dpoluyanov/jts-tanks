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

package ru.jts.common.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author : Camelion
 * @date : 18.08.12  1:12
 */
public abstract class AbstractHolder {
	private static final Logger log = LoggerFactory.getLogger(AbstractHolder.class);
	private long parseStartTime, parseEndTime;

	/**
	 * Вызывается непосредственно перед загрузкой
	 */
	public void beforeParsing() {
		parseStartTime = System.nanoTime();
	}

	/**
	 * Вызывается после того, как были загружены все элементы.
	 */
	public void afterParsing() {
		parseEndTime = System.nanoTime();
	}

	public void logAfterLoading() {
		log.info(String.format("%s: Loaded %d elements (in %.3f sec)", getClass().getSimpleName(), size(), (float) TimeUnit.NANOSECONDS.toMillis(parseEndTime - parseStartTime) / 1000D));
	}

	public abstract int size();
}

