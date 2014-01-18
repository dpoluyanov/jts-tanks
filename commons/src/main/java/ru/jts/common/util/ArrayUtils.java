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

package ru.jts.common.util;

import java.util.Formatter;

/**
 * @author : Camelion
 * @date : 26.08.12  14:26
 */
public final class ArrayUtils {
	public static final int[] EMPTY_INT_ARRAY = new int[0];
	public static final String[] EMPTY_STRING_ARRAY = {};

	public static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);

		Formatter formatter = new Formatter(sb);
		for (byte b : bytes) {
			formatter.format("%02x ", b);
		}

		return sb.toString().substring(0, sb.length() - 1).toUpperCase();
	}
}
