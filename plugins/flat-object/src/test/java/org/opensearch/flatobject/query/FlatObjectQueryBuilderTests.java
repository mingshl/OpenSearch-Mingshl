/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.flatobject.query;

import org.apache.lucene.search.Query;
import org.opensearch.flatobject.FlatObjectPlugin;
import org.opensearch.index.query.QueryShardContext;
import org.opensearch.plugins.Plugin;
import org.opensearch.test.AbstractQueryTestCase;
import org.opensearch.test.transport.MockTransportService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class FlatObjectQueryBuilderTests extends AbstractQueryTestCase<FlatObjectQueryBuilder> {

    @Override
    protected Collection<Class<? extends Plugin>> getPlugins() {
        return Arrays.asList(FlatObjectPlugin.class, MockTransportService.TestPlugin.class);
    }

    @Override
    protected FlatObjectQueryBuilder doCreateTestQueryBuilder() {
        return null;
    }

    @Override
    protected void doAssertLuceneQuery(FlatObjectQueryBuilder queryBuilder, Query query, QueryShardContext context) throws IOException {

    }
}
