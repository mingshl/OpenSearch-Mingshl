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
import org.opensearch.index.mapper.FieldMapperTestCase2;
import org.opensearch.plugins.Plugin;

import java.io.IOException;
import java.util.Collection;

public class FlatObjectFieldMapperTests extends FieldMapperTestCase2<FlatObjectFieldMapper.Builder> {
    private static final String FIELD_TYPE = "flat-object";

    @Override
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
        // TODO
    }

    @Override
    public void testMergeConflicts(){
        // TODO, Merge is not implemented for flat-object yet
    }

    @Override
    public void testMeta(){
        // TODO, parse_values is not implemented for flat-object yet
    }

    @Override
    public void testDeprecatedBoost(){
        // TODO, [boost : 2.0] is not determined for flat-object yet,
        // might need to overwrite this test
    }

}
