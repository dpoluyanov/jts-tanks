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

package ru.jts.common.math;

/**
 * @author : Camelion
 * @date : 16.11.11   9:54
 */
public class Rnd {
    private static final MTRandom random = new MTRandom(false);

    /**
     * Возвращает случайное число от 0(включительно) до n(исключая)
     *
     * @param n - верхний предел
     * @return случайное число от 0 до n-1
     */
    public static int get(int n) {
        return (int) Math.floor(random.nextDouble() * n);
    }

    public static int nextInt() {
        return random.nextInt();
    }
}
