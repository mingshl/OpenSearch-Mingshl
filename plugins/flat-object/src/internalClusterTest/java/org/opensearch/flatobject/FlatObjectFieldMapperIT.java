/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.flatobject;

import org.opensearch.common.xcontent.XContentBuilder;
import org.opensearch.plugins.Plugin;
import org.opensearch.test.OpenSearchIntegTestCase;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.opensearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.opensearch.test.hamcrest.OpenSearchAssertions.assertAcked;

public class FlatObjectFieldMapperIT extends OpenSearchIntegTestCase {

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singletonList(FlatObjectPlugin.class);
    }

    public void testFlatObject() throws IOException {
        String index = "test";

        XContentBuilder builder = jsonBuilder().startObject()
            .startObject("properties")
            .startObject("catalog")
            .field("type", "flat-object")
            .endObject()
            .endObject()
            .endObject();

        assertAcked(client().admin().indices().prepareCreate(index).setMapping(builder));
    }
}
