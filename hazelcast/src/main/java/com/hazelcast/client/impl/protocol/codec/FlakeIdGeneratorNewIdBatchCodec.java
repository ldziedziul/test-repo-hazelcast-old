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
 * TODO DOC
 */
public final class FlakeIdGeneratorNewIdBatchCodec {
    //hex: 0x1F01
    public static final int REQUEST_MESSAGE_TYPE = 7937;
    //hex: 0x007E
    public static final int RESPONSE_MESSAGE_TYPE = 126;
    private static final int REQUEST_BATCH_SIZE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_BATCH_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_BASE_FIELD_OFFSET = CORRELATION_ID_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int RESPONSE_INCREMENT_FIELD_OFFSET = RESPONSE_BASE_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int RESPONSE_BATCH_SIZE_FIELD_OFFSET = RESPONSE_INCREMENT_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_BATCH_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;

    private FlakeIdGeneratorNewIdBatchCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * TODO DOC
         */
        public java.lang.String name;

        /**
         * TODO DOC
         */
        public int batchSize;
    }

    public static ClientMessage encodeRequest(java.lang.String name, int batchSize) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(true);
        clientMessage.setAcquiresResource(false);
        clientMessage.setOperationName("FlakeIdGenerator.NewIdBatch");
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, REQUEST_BATCH_SIZE_FIELD_OFFSET, batchSize);
        clientMessage.addFrame(initialFrame);
        StringCodec.encode(clientMessage, name);
        return clientMessage;
    }

    public static FlakeIdGeneratorNewIdBatchCodec.RequestParameters decodeRequest(ClientMessage clientMessage) {
        ListIterator<ClientMessage.Frame> iterator = clientMessage.iterator();
        RequestParameters request = new RequestParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        request.batchSize = decodeInt(initialFrame.content, REQUEST_BATCH_SIZE_FIELD_OFFSET);
        request.name = StringCodec.decode(iterator);
        return request;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * TODO DOC
         */
        public long base;

        /**
         * TODO DOC
         */
        public long increment;

        /**
         * TODO DOC
         */
        public int batchSize;
    }

    public static ClientMessage encodeResponse(long base, long increment, int batchSize) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        clientMessage.addFrame(initialFrame);

        encodeLong(initialFrame.content, RESPONSE_BASE_FIELD_OFFSET, base);
        encodeLong(initialFrame.content, RESPONSE_INCREMENT_FIELD_OFFSET, increment);
        encodeInt(initialFrame.content, RESPONSE_BATCH_SIZE_FIELD_OFFSET, batchSize);
        return clientMessage;
    }

    public static FlakeIdGeneratorNewIdBatchCodec.ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ListIterator<ClientMessage.Frame> iterator = clientMessage.iterator();
        ResponseParameters response = new ResponseParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        response.base = decodeLong(initialFrame.content, RESPONSE_BASE_FIELD_OFFSET);
        response.increment = decodeLong(initialFrame.content, RESPONSE_INCREMENT_FIELD_OFFSET);
        response.batchSize = decodeInt(initialFrame.content, RESPONSE_BATCH_SIZE_FIELD_OFFSET);
        return response;
    }

}
