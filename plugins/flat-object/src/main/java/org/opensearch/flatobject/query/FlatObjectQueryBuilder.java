/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.flatobject.query;

import org.apache.lucene.search.Query;
import org.opensearch.common.ParsingException;
import org.opensearch.common.Strings;
import org.opensearch.common.io.stream.StreamInput;
import org.opensearch.common.io.stream.StreamOutput;
import org.opensearch.common.xcontent.XContentBuilder;
import org.opensearch.common.xcontent.XContentParser;
import org.opensearch.index.query.AbstractQueryBuilder;
import org.opensearch.index.query.QueryBuilder;
import org.opensearch.index.query.QueryShardContext;
import org.opensearch.index.query.TempWorkaround;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FlatObjectQueryBuilder extends AbstractQueryBuilder<FlatObjectQueryBuilder> {

    public static final String NAME = "flat-object";

    private final String fieldName;
    private final List<?> values;

    public FlatObjectQueryBuilder(String fieldName, Object... values) {
        this(fieldName, values != null ? Arrays.asList(values) : (Iterable<?>) null);
    }

    public FlatObjectQueryBuilder(String fieldName, Iterable<?> values) {
        if (Strings.isEmpty(fieldName)) {
            throw new IllegalArgumentException("field name cannot be null.");
        }
        if (values == null) {
            throw new IllegalArgumentException("No value specified for flat-object query.");
        }
        this.fieldName = fieldName;
        this.values = values == null ? null : TempWorkaround.convert(values);
    }

    public FlatObjectQueryBuilder(StreamInput in) throws IOException {
        super(in);
        fieldName = in.readString();
        values = (List<?>) in.readGenericValue();
    }

    /**
     * Constructs a query, considering two cases
     * if without specifying the path of the value, query in value only stringField,
     * else (with dot path), query in content_and_path stringField
     */

    @Override
    public String getWriteableName() {
        return NAME;
    }

    @Override
    protected void doWriteTo(StreamOutput out) throws IOException {
        out.writeString(fieldName);
        out.writeGenericValue(values);
    }

    @Override
    protected Query doToQuery(QueryShardContext context) throws IOException {
        // TODO
        return null;
    }

    @Override
    protected boolean doEquals(FlatObjectQueryBuilder other) {
        return Objects.equals(fieldName, other.fieldName) && Objects.equals(values, other.values);
    }

    @Override
    protected int doHashCode() {
        return Objects.hash(fieldName, values);
    }

    @Override
    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(NAME);
        builder.field(fieldName, TempWorkaround.convertBack(values));
        printBoostAndQueryName(builder);
        builder.endObject();
    }

    public static QueryBuilder fromXContent(XContentParser parser) throws IOException {
        String fieldName = null;
        List<Object> values = null;

        float boost = AbstractQueryBuilder.DEFAULT_BOOST;
        String type = null;
        String id = null;
        String queryName = null;

        // TODO depends on parser methods
        XContentParser.Token token;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.START_ARRAY) {
                if (fieldName != null) {
                    throw new ParsingException(
                        parser.getTokenLocation(),
                        "[" + FlatObjectQueryBuilder.NAME + "] query does not support multiple fields"
                    );
                }
                fieldName = currentFieldName;
                values = TempWorkaround.parseValues(parser);
            } else if (token.isValue()) {
                // Boost support?
                if (AbstractQueryBuilder.BOOST_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    boost = parser.floatValue();
                } else if (AbstractQueryBuilder.NAME_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    queryName = parser.text();
                } else {
                    throw new ParsingException(
                        parser.getTokenLocation(),
                        "[" + FlatObjectQueryBuilder.NAME + "] query does not support [" + currentFieldName + "]"
                    );
                }
            } else {
                throw new ParsingException(
                    parser.getTokenLocation(),
                    "[" + FlatObjectQueryBuilder.NAME + "] unknown token [" + token + "] after [" + currentFieldName + "]"
                );
            }
        }

        if (fieldName == null) {
            throw new ParsingException(
                parser.getTokenLocation(),
                "[" + FlatObjectQueryBuilder.NAME + "] query requires a field name, " + "followed by array"
            );
        }

        return new FlatObjectQueryBuilder(fieldName, values).queryName(queryName).boost(boost);
    }
}
