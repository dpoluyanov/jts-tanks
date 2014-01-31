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

package ru.jts.authserver.network.clientpackets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ru.jts.authserver.controllers.AccountController;
import ru.jts.authserver.database.AccountsDAO;
import ru.jts.authserver.model.Account;
import ru.jts.authserver.network.Client;
import ru.jts.authserver.network.crypt.CryptEngine;
import ru.jts.authserver.network.serverpackets.SM_AUTH_RESPONSE;
import ru.jts.common.enums.State;
import ru.jts.common.math.Rnd;
import ru.jts.common.network.ClientPacket;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Camelion, Grizly(Skype: r-grizly)
 * @date: 02.11.13/0:43
 * @last: 1.02.2014
 */
//TODO Клиент очень сильно флудит этим пакетом надо сделать чтобы прошел один раз, остальные игнорил.
public class CM_AUTH_BY_PASS extends ClientPacket<Client> {
	private static final String JSON_LOGIN = "login";

	private final short sessionId;
	private byte[] blowFishKey;
	private Map<String, String> jsonMap;
    String password;

	public CM_AUTH_BY_PASS(short sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public void readImpl() {
		// JSON Message
		//// session = md5Hex of Client HWID
		jsonMap = readJson();
		byte passLength = readByte();
        password = readString(passLength); // plain pass
		byte blowFishLength = readByte();
		blowFishKey = readBytes(blowFishLength);
		byte[] unk = readBytes(16); // unknown
		short unk2 = readShort(); // неизвестно, изменяется при каждом новом подключении
		readShort();
	}

	@Override
	public void runImpl() {
		//Account account = AccountsDAO.getInstance().restoreByLogin(jsonMap.get(JSON_LOGIN));

		getClient().setBlowFishKey(blowFishKey);
		getClient().setRandomKey(Rnd.nextInt());

		ByteBuf buf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

		buf.writeBytes(new byte[]{(byte) 192, (byte) 168, (byte) 1, (byte) 100});
		buf.writeInt(20015);

		buf.writeInt(getClient().getRandomKey());

		Map<String, String> jsonMapp = new HashMap<>();
		//jsonMap.put("security_msg", "old_pass");
		jsonMapp.put("token2", getClient().generateToken2());

		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(jsonMapp);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return;
		}
		buf.writeByte(json.length());
		buf.writeBytes(json.getBytes());

		byte[] cryptedData = CryptEngine.getInstance().encrypt(buf.copy().array(), getClient().getBlowFishKey(), CryptEngine.ZERO_TRAILING_MODE);


        State state = AccountController.getInstance().accountLogin(jsonMap, password);
        switch (state) {
            case AUTHED: {
                getClient().sendPacket(new SM_AUTH_RESPONSE(sessionId, SM_AUTH_RESPONSE.LOGIN_OK, cryptedData));
                break;
            }
            case ALREADY_AUTHED: {
                getClient().sendPacket(new SM_AUTH_RESPONSE(sessionId, SM_AUTH_RESPONSE.LOGIN_REJECTED_ALREADY_LOGGED_IN, cryptedData));
                break;
            }
            default: {
            }
        }
	}
}
