/*
 * Copyright (c) 2008-2023, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.map.impl.querycache.subscriber.record;

import com.hazelcast.internal.serialization.Data;
import com.hazelcast.internal.serialization.SerializationService;

/**
 * Factory for {@link DataQueryCacheRecord}.
 *
 * @see DataQueryCacheRecord
 */
public class DataQueryCacheRecordFactory implements QueryCacheRecordFactory {

    private final SerializationService serializationService;

    public DataQueryCacheRecordFactory(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    @Override
    public QueryCacheRecord createRecord(Data valueData) {
        return new DataQueryCacheRecord(valueData, serializationService);
    }

    @Override
    public boolean isEquals(Object value1, Object value2) {
        return serializationService.toData(value1).equals(serializationService.toData(value2));
    }
}
