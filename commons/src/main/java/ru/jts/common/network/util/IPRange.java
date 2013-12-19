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

package ru.jts.common.network.util;

/**
 * @author : Camelion
 * @date : 17.08.12  22:43
 * <p/>
 * Класс-помощник для создания перенаправлений.
 * Что-то подобное используется в Aion-emu, L2J, L2P, etc.
 */
public class IPRange {

    private long minAddress;
    private long maxAddress;
    private byte[] minAddressInByte;
    private byte[] maxAddressInByte;
    private byte[] routeAddress;

    public IPRange(String minAddress, String maxAddress, String routeAddress) {
        this.minAddress = toLong(minAddress);
        this.maxAddress = toLong(maxAddress);
        this.minAddressInByte = toByteArray(minAddress);
        this.maxAddressInByte = toByteArray(maxAddress);
        this.routeAddress = toByteArray(routeAddress);
    }

    public IPRange(byte[] minAddress, byte[] maxAddress, byte[] routeAddress) {
        this.minAddress = toLong(minAddress);
        this.maxAddress = toLong(maxAddress);
        this.minAddressInByte = minAddress;
        this.maxAddressInByte = maxAddress;
        this.routeAddress = routeAddress;
    }

    /**
     * Разбивает адрес типа IPv4 на массив длиной 4
     * Например 127.0.0.1 = {127, 0, 0, 1};
     *
     * @param address - адрес в формате IPv4
     * @return - массив байтов, содержащий данный адрес
     */
    public static byte[] toByteArray(String address) {
        byte[] result = new byte[4];
        String[] strings = address.split("\\.");
        for (int i = 0, n = strings.length; i < n; i++) {
            result[i] = (byte) Integer.parseInt(strings[i]);
        }

        return result;
    }

    /**
     * Переводит IPv4 адрес в число типа long
     *
     * @param address - String адрес в формате IPv4
     * @return число типа long
     */
    public long toLong(String address) {
        byte[] byteAddress = toByteArray(address);
        long result = 0;
        result += (byteAddress[3] & 0xFF);
        result += ((byteAddress[2] & 0xFF) << 8);
        result += ((byteAddress[1] & 0xFF) << 16);
        result += (byteAddress[0] << 24);
        return result & 0xFFFFFFFFL;
    }

    /**
     * Переводит IPv4 адрес в число типа long
     *
     * @param address - byte[] адрес в формате IPv4
     * @return число типа long
     */
    public long toLong(byte[] address) {
        long result = 0;
        result += (address[3] & 0xFF);
        result += ((address[2] & 0xFF) << 8);
        result += ((address[1] & 0xFF) << 16);
        result += (address[0] << 24);
        return result & 0xFFFFFFFFL;
    }


    /**
     * Проверяет, находится ли адрес в заданом диапазоне адресов
     *
     * @param address - проверяемый адрес
     * @return - true, если адрес попадает в диапазон, иначе - false
     */
    public boolean isInRange(String address) {
        long addr = toLong(address);
        return addr >= minAddress && addr <= maxAddress;
    }

    public byte[] getRouteAddress() {
        return routeAddress;
    }

    public byte[] getMinAddressInByte() {
        return minAddressInByte;
    }

    public byte[] getMaxAddressInByte() {
        return maxAddressInByte;
    }
}
