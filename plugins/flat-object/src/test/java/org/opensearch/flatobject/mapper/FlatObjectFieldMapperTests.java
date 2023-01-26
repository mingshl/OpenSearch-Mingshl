/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.flatobject.mapper;

import org.opensearch.common.xcontent.XContentBuilder;
import org.opensearch.index.mapper.FieldMapperTestCase2;

import java.io.IOException;

public class FlatObjectFieldMapperTests extends FieldMapperTestCase2<FlatObjectFieldMapper.Builder> {
    private static final String FIELD_TYPE = "flat-object";

    @Override
    protected FlatObjectFieldMapper.Builder newBuilder() {
        return new FlatObjectFieldMapper.Builder("flat-object");
    }

    @Override
    protected void minimalMapping(XContentBuilder b) throws IOException {
        b.field("type", FIELD_TYPE);
    }

    /**
     * Writes a sample value for the field to the provided {@link XContentBuilder}.
     *
     * @param builder builder
     */
    @Override
    protected void writeFieldValue(XContentBuilder builder) throws IOException {
        builder.value("{}");
    }

    @Override
    protected void registerParameters(ParameterChecker checker) throws IOException {
        // TODO
    }
}
