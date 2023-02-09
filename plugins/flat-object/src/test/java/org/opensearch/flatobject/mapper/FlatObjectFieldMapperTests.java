/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.flatobject.mapper;

import org.opensearch.common.xcontent.XContentBuilder;
import org.opensearch.flatobject.FlatObjectPlugin;
import org.opensearch.index.mapper.MapperTestCase;
import org.opensearch.plugins.Plugin;

import java.io.IOException;
import java.util.Collection;

public class FlatObjectFieldMapperTests extends MapperTestCase {
    private static final String FIELD_TYPE = "flat-object";

    // @Override
    public FlatObjectFieldMapper.Builder newBuilder() {
        return new FlatObjectFieldMapper.Builder("flat-object");
    }

    @Override
    protected Collection<? extends Plugin> getPlugins() {
        return org.opensearch.common.collect.List.of(new FlatObjectPlugin());
    }

    @Override
    public void minimalMapping(XContentBuilder b) throws IOException {
        b.field("type", FIELD_TYPE);
    }

    /**
     * Writes a sample value for the field to the provided {@link XContentBuilder}.
     *
     * @param builder builder
     */
    @Override
    protected void writeFieldValue(XContentBuilder builder) throws IOException {
        builder.value("value");
    }

    @Override
    protected void registerParameters(ParameterChecker checker) throws IOException {
        checker.registerConflictCheck("doc_values", b -> b.field("doc_values", false));
        checker.registerConflictCheck("index", b -> b.field("index", false));
        checker.registerConflictCheck("store", b -> b.field("store", true));
        checker.registerConflictCheck("index_options", b -> b.field("index_options", "freqs"));
        checker.registerConflictCheck("null_value", b -> b.field("null_value", "foo"));
        checker.registerConflictCheck("similarity", b -> b.field("similarity", "boolean"));
        checker.registerConflictCheck("normalizer", b -> b.field("normalizer", "lowercase"));

        checker.registerUpdateCheck(b -> b.field("eager_global_ordinals", true), m -> assertTrue(m.fieldType().eagerGlobalOrdinals()));
        checker.registerUpdateCheck(b -> b.field("ignore_above", 256), m -> assertEquals(256, ((FlatObjectFieldMapper) m).ignoreAbove()));
        checker.registerUpdateCheck(
            b -> b.field("split_queries_on_whitespace", true),
            m -> assertEquals("_whitespace", m.fieldType().getTextSearchInfo().getSearchAnalyzer().name())
        );

        // norms can be set from true to false, but not vice versa
        checker.registerConflictCheck("norms", b -> b.field("norms", true));
        checker.registerUpdateCheck(b -> {
            b.field("type", "flat-object");
            b.field("norms", true);
        }, b -> {
            b.field("type", "flat-object");
            b.field("norms", false);
        }, m -> assertFalse(m.fieldType().getTextSearchInfo().hasNorms()));

        checker.registerUpdateCheck(b -> b.field("boost", 2.0), m -> assertEquals(m.fieldType().boost(), 2.0, 0));
    }

    @Override
    public void testMinimalToMaximal() throws IOException {
        // ToDO [299 OpenSearch-3.0.0-SNAPSHOT-unknown "Parameter [boost] on field [field] is deprecated and will be removed in 8.0"]

    }

    @Override
    public void testUpdates() throws IOException {
        // Todo

    }

}
