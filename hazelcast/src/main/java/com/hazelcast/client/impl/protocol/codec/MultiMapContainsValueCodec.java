/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.hazelcast.client.impl.protocol.codec;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.builtin.*;

import java.util.ListIterator;

import static com.hazelcast.client.impl.protocol.ClientMessage.*;
import static com.hazelcast.client.impl.protocol.codec.builtin.FixedSizeTypesCodec.*;

/**
 * Returns whether the multimap contains an entry with the value.
 */
public final class MultiMapContainsValueCodec {
    //hex: 0x0208
    public static final int REQUEST_MESSAGE_TYPE = 520;
    //hex: 0x0065
    public static final int RESPONSE_MESSAGE_TYPE = 101;
    private static final int REQUEST_INITIAL_FRAME_SIZE = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_RESPONSE_FIELD_OFFSET = CORRELATION_ID_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_RESPONSE_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;

    private MultiMapContainsValueCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Name of the MultiMap
         */
        public java.lang.String name;

        /**
         * The value whose existence is checked.
         */
        public com.hazelcast.nio.serialization.Data value;
    }

    public static ClientMessage encodeRequest(java.lang.String name, com.hazelcast.nio.serialization.Data value) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(true);
        clientMessage.setAcquiresResource(false);
        clientMessage.setOperationName("MultiMap.ContainsValue");
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        clientMessage.addFrame(initialFrame);
        StringCodec.encode(clientMessage, name);
        DataCodec.encode(clientMessage, value);
        return clientMessage;
    }

    public static MultiMapContainsValueCodec.RequestParameters decodeRequest(ClientMessage clientMessage) {
        ListIterator<ClientMessage.Frame> iterator = clientMessage.iterator();
        RequestParameters request = new RequestParameters();
        //empty initial frame
        iterator.next();
        request.name = StringCodec.decode(iterator);
        request.value = DataCodec.decode(iterator);
        return request;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * TODO DOC
         */
        public boolean response;
    }

    public static ClientMessage encodeResponse(boolean response) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        clientMessage.addFrame(initialFrame);

        encodeBoolean(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET, response);
        return clientMessage;
    }

    public static MultiMapContainsValueCodec.ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ListIterator<ClientMessage.Frame> iterator = clientMessage.iterator();
        ResponseParameters response = new ResponseParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        response.response = decodeBoolean(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET);
        return response;
    }

}
