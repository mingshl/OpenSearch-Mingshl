/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 *
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

package org.opensearch.flatobject;

import org.opensearch.flatobject.mapper.FlatObjectFieldMapper;
import org.opensearch.index.mapper.Mapper;
import org.opensearch.plugins.MapperPlugin;
import org.opensearch.plugins.Plugin;
import org.opensearch.plugins.SearchPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Flat Object plugin.
 *
 * @see <a href="https://github.com/opensearch-project/OpenSearch/issues/1018">Feature request #1018</a>
 *
 * @opensearch.experimental
 */
public class FlatObjectPlugin extends Plugin implements SearchPlugin, MapperPlugin {

    public FlatObjectPlugin() {}

    @Override
    public List<QuerySpec<?>> getQueries() {
        return SearchPlugin.super.getQueries();
    }

    @Override
    public Map<String, Mapper.TypeParser> getMappers() {
        return Collections.singletonMap(FlatObjectFieldMapper.CONTENT_TYPE, new FlatObjectFieldMapper.TypeParser());
    }
}
