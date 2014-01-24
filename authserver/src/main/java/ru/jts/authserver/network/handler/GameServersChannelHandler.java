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

package ru.jts.authserver.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import ru.jts.common.util.ArrayUtils;

/**
 * @author: Camelion
 * @date: 21.01.14/2:45
 */
public class GameServersChannelHandler extends LengthFieldBasedFrameDecoder {
	public GameServersChannelHandler() {
		super(Integer.MAX_VALUE, 0, 2, 2, 0);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf buf = (ByteBuf) super.decode(ctx, in);
		System.out.println(ArrayUtils.bytesToHexString(buf.array()));
		return buf;
	}
}
