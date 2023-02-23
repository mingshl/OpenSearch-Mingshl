/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.index.query;

import org.opensearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is just a temporary class to expose methods that are package restricted.
 * In the future this class will be deleted.
 */
public class TempWorkaround {

    /**
     * Borrowed from private <code>TermsQueryBuilder.convert(Iterable values)</code>
     * which in the end calls package protected <code>TermsQueryBuilder.convert(list)</code>.
     *
     * @param values An input list of values
     * @return Possibly optimized representation of the input values
     */
    public static List<?> convert(Iterable<?> values) {
        List<?> list;
        if (values instanceof List<?>) {
            list = (List<?>) values;
        } else {
            ArrayList<Object> arrayList = new ArrayList<>();
            for (Object o : values) {
                arrayList.add(o);
            }
            list = arrayList;
        }
        return TermsQueryBuilder.convert(list);
    }

    /**
     * Exposes package protected <code>TermsQueryBuilder.convertBack(list)</code>.
     *
     * @param list A list of values
     * @return List of values converted to user-friendly representation.
     */
    public static List<Object> convertBack(List<?> list) {
        return TermsQueryBuilder.convertBack(list);
    }

    /**
     * Exposes package protected <code>TermsQueryBuilder.parseValues(parser)</code>.
     *
     * @param parser A parser with a client data.
     * @return Possibly converted client data to user-friendly representation
     * @throws IOException When something goes wrong
     */
    public static List<Object> parseValues(XContentParser parser) throws IOException {
        return TermsQueryBuilder.parseValues(parser);
    }
}
