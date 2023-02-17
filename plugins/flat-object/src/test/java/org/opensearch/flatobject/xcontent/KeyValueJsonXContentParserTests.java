/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.flatobject.xcontent;

import org.opensearch.common.bytes.BytesReference;
import org.opensearch.common.xcontent.DeprecationHandler;
import org.opensearch.common.xcontent.NamedXContentRegistry;
import org.opensearch.common.xcontent.XContentBuilder;
import org.opensearch.common.xcontent.XContentHelper;
import org.opensearch.common.xcontent.XContentParser;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.common.xcontent.json.JsonXContent;
import org.opensearch.flatobject.mapper.FlatObjectFieldMapper;
import org.opensearch.test.OpenSearchTestCase;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class KeyValueJsonXContentParserTests extends OpenSearchTestCase {
    private static final Logger logger = Logger.getLogger((FlatObjectFieldMapper.class.getName()));

    public void testKeyValueJsonXContentParser() throws IOException {

        NamedXContentRegistry xContentRegistry = NamedXContentRegistry.EMPTY;
        DeprecationHandler deprecationHandler = DeprecationHandler.IGNORE_DEPRECATIONS;

        try (XContentBuilder builder = XContentBuilder.builder(JsonXContent.jsonXContent)) {
            builder.startObject();
            builder.field("catalog", "{title: Lucene in Action}");
            builder.endObject();
            String jString = XContentHelper.convertToJson(BytesReference.bytes(builder), false, XContentType.JSON);

            String value;
            String fieldName;

            try (XContentParser parser = JsonXContent.jsonXContent.createParser(xContentRegistry, deprecationHandler, jString)) {

                XContentParser.Token currentToken;
                while ((currentToken = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                    switch (currentToken) {
                        case FIELD_NAME:
                            fieldName = parser.currentName();
                            assertEquals(fieldName, "catalog");

                            break;
                        case VALUE_STRING:
                            value = parser.textOrNull();
                            assertEquals(value, "{title: Lucene in Action}");
                            break;

                    }
                }

            }

        }
    }
}
