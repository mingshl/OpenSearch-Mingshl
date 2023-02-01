/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.flatobject.query;
import org.apache.lucene.search.Query;
import org.opensearch.common.io.stream.StreamOutput;
import org.opensearch.common.xcontent.XContentBuilder;
import org.opensearch.common.xcontent.XContentParser;
import org.opensearch.index.query.AbstractQueryBuilder;
import org.opensearch.index.query.QueryBuilder;
import org.opensearch.index.query.QueryShardContext;

import java.io.IOException;


public class FlatObjectQueryBuilder extends AbstractQueryBuilder<FlatObjectQueryBuilder> {

    public static final String NAME = "flat-object";

    /**
     * Constructs a query, considering two cases
     * if without specifying the path of the value, query in value only stringField,
     * else (with dot path), query in content_and_path stringField
     */

    @Override
    public String getWriteableName() {
        return null;
    }

    @Override
    protected void doWriteTo(StreamOutput out) throws IOException {
    }

    @Override
    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
        //TODO
    }

    @Override
    protected Query doToQuery(QueryShardContext context) throws IOException {
        //TODO
        return null;
    }

    @Override
    protected boolean doEquals(FlatObjectQueryBuilder other) {
        //TODO
        return false;
    }

    @Override
    protected int doHashCode() {
        //TODO
        return 0;
    }


    public static QueryBuilder fromXContent(XContentParser xContentParser)  throws IOException {
        float boost = AbstractQueryBuilder.DEFAULT_BOOST;
        String type = null;
        String id = null;
        String queryName = null;
        String currentFieldName = null;
        XContentParser.Token token;

        //TODO depends on parser methods

        FlatObjectQueryBuilder queryBuilder = new FlatObjectQueryBuilder();
        queryBuilder.queryName(queryName);
        queryBuilder.boost(boost);
        return queryBuilder;
    }
}
