/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.benchmark.indices.mapper;

import org.apache.hc.core5.http.HttpHost;
import org.json.JSONObject;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.opensearch.action.admin.indices.delete.DeleteIndexRequest;
import org.opensearch.action.admin.indices.refresh.RefreshRequest;
import org.opensearch.action.admin.indices.refresh.RefreshResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.action.support.master.AcknowledgedResponse;
import org.opensearch.benchmark.indices.mapper.FlatObjectMappingBenchmark.MyState;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.CreateIndexResponse;
import org.opensearch.common.xcontent.XContentFactory;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHits;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.opensearch.search.sort.SortOrder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 100, time = 1, timeUnit = TimeUnit.SECONDS)

public class FlatObjectMappingBenchmark {

    @State(Scope.Thread)
    public static class MyState {
        private RestHighLevelClient client;

        @Setup(Level.Trial)
        public void setup() throws Exception {
            String httpUri = System.getProperty("opensearch.uri", "http://localhost:9200");
            if (httpUri == null || httpUri.trim().isEmpty()) {
                throw new IllegalArgumentException("opensearch.uri system property not set");
            }

            this.client = new RestHighLevelClient(RestClient.builder(HttpHost.create(httpUri)));

        }

        @TearDown(Level.Trial)
        public void tearDown() throws Exception {
            this.client.close();
        }
    }

    /**
     * DynamicIndex:
     * create index and delete index
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void CreateDynamicIndex(MyState state) throws IOException, URISyntaxException {
        GetDynamicIndex(state, "demo-dynamic-test");
        DeleteIndex(state, "demo-dynamic-test");
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void CreateFlatObjectIndex(MyState state) throws IOException, URISyntaxException {
        GetFlatObjectIndex(state, "demo-flat-object-test", "host");
        DeleteIndex(state, "demo-flat-object-test");
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void indexDynamicMapping(MyState state) throws IOException, URISyntaxException {
        GetDynamicIndex(state, "demo-dynamic-test1");
        String doc =
            "{ \"message\": \"[5592:1:0309/123054.737712:ERROR:child_process_sandbox_support_impl_linux.cc(79)] FontService unique font name matching request did not receive a response.\", \"fileset\": { \"name\": \"syslog\" }, \"process\": { \"name\": \"org.gnome.Shell.desktop\", \"pid\": 3383 }, \"@timestamp\": \"2020-03-09T18:00:54.000+05:30\", \"host\": { \"hostname\": \"bionic\", \"name\": \"bionic\" } }";
        UploadDoc(state, "demo-dynamic-test1", doc);
        DeleteIndex(state, "demo-dynamic-test1");
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void indexFlatObjectMapping(MyState state) throws IOException, URISyntaxException {
        GetFlatObjectIndex(state, "demo-flat-object-test1", "host");
        String doc =
            "{ \"message\": \"[5592:1:0309/123054.737712:ERROR:child_process_sandbox_support_impl_linux.cc(79)] FontService unique font name matching request did not receive a response.\", \"fileset\": { \"name\": \"syslog\" }, \"process\": { \"name\": \"org.gnome.Shell.desktop\", \"pid\": 3383 }, \"@timestamp\": \"2020-03-09T18:00:54.000+05:30\", \"host\": { \"hostname\": \"bionic\", \"name\": \"bionic\" } }";
        UploadDoc(state, "demo-flat-object-test1", doc);
        DeleteIndex(state, "demo-flat-object-test1");
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void searchDynamicMapping(MyState state) throws IOException {
        String indexName = "demo-dynamic-test2";
        GetDynamicIndex(state, indexName);
        String doc =
            "{ \"message\": \"[5592:1:0309/123054.737712:ERROR:child_process_sandbox_support_impl_linux.cc(79)] FontService unique font name matching request did not receive a response.\", \"fileset\": { \"name\": \"syslog\" }, \"process\": { \"name\": \"org.gnome.Shell.desktop\", \"pid\": 3383 }, \"@timestamp\": \"2020-03-09T18:00:54.000+05:30\", \"host\": { \"hostname\": \"bionic\", \"name\": \"bionic\" } }";
        UploadDoc(state, indexName, doc);
        SearchDoc(state, indexName, "host.hostname", "bionic", "@timestamp", "message");
        DeleteIndex(state, indexName);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void searchFlatObjectMapping(MyState state) throws IOException {
        GetFlatObjectIndex(state, "demo-flat-object-test2", "host");
        String doc =
            "{ \"message\": \"[5592:1:0309/123054.737712:ERROR:child_process_sandbox_support_impl_linux.cc(79)] FontService unique font name matching request did not receive a response.\", \"fileset\": { \"name\": \"syslog\" }, \"process\": { \"name\": \"org.gnome.Shell.desktop\", \"pid\": 3383 }, \"@timestamp\": \"2020-03-09T18:00:54.000+05:30\", \"host\": { \"hostname\": \"bionic\", \"name\": \"bionic\" } }";
        UploadDoc(state, "demo-flat-object-test2", doc);
        SearchDoc(state, "demo-flat-object-test2", "host", "bionic", "@timestamp", "message");
        DeleteIndex(state, "demo-flat-object-test2");
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void searchDynamicMappingWithOneHundredNestedJSON(MyState state) throws IOException {

        String indexName = "demo-dynamic-test3";
        DeleteIndex(state, indexName);
        GetDynamicIndex(state, indexName);
        String doc = GenerateRandomJson();
        // String searchValue = getNestedValue(doc,99);
        Map<String, String> searchValueAndPath = findNestedValueAndPath(doc, 99, "field0");
        String searchValue = searchValueAndPath.get("value");
        String searchFieldName = searchValueAndPath.get("path");
        // String doc = "{ \"message\": \"[5592:1:0309/123054.737712:ERROR:child_process_sandbox_support_impl_linux.cc(79)] FontService
        // unique font name matching request did not receive a response.\", \"fileset\": { \"name\": \"syslog\" }, \"process\": { \"name\":
        // \"org.gnome.Shell.desktop\", \"pid\": 3383 }, \"@timestamp\": \"2020-03-09T18:00:54.000+05:30\", \"host\": { \"hostname\":
        // \"bionic\", \"name\": \"bionic\" } }";
        UploadDoc(state, indexName, doc);
        SearchDoc(state, indexName, searchFieldName, searchValue, searchValue, searchFieldName);
        DeleteIndex(state, indexName);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void searchFlatObjectMappingWithOneHundredNestedJSON(MyState state) throws IOException {

        String indexName = "demo-flat-object-test3";
        // DeleteIndex(state, indexName);
        GetFlatObjectIndex(state, indexName, "nested0");
        String doc = GenerateRandomJson();
        Map<String, String> searchValueAndPath = findNestedValueAndPath(doc, 99, "nested0");
        String searchFieldName = "nested0";
        UploadDoc(state, indexName, doc);
        SearchDoc(state, indexName, searchFieldName, "field40.4", searchFieldName, searchFieldName);
        DeleteIndex(state, indexName);
    }

    private static void GetDynamicIndex(MyState state, String indexName) throws IOException {
        CreateIndexRequest dynamicRequest = new CreateIndexRequest(indexName);
        CreateIndexResponse dynamicResponse = state.client.indices().create(dynamicRequest, RequestOptions.DEFAULT);
        if (!dynamicResponse.isAcknowledged()) {
            System.out.println("Failed to create index");
        }
    }

    private static void GetFlatObjectIndex(MyState state, String indexName, String flatObjectFieldName) throws IOException {
        CreateIndexRequest flatRequest = new CreateIndexRequest(indexName).mapping(
            XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject(flatObjectFieldName)
                .field("type", "flat-object")
                .endObject()
                .endObject()
                .endObject()
        );

        CreateIndexResponse flatResponse = state.client.indices().create(flatRequest, RequestOptions.DEFAULT);

        if (flatResponse.isAcknowledged()) {} else {
            System.out.println("Failed to create index");
        }
    }

    private static void DeleteIndex(MyState state, String indexName) throws IOException {
        DeleteIndexRequest dynamicDeleteRequest = new DeleteIndexRequest(indexName);
        AcknowledgedResponse dynamicDeleteResponse = state.client.indices().delete(dynamicDeleteRequest, RequestOptions.DEFAULT);
        if (dynamicDeleteResponse.isAcknowledged()) {} else {
            System.out.println("Failed to delete index");
        }
    }

    private static void UploadDoc(MyState state, String indexName, String doc) throws IOException {
        IndexRequest request = new IndexRequest(indexName);
        request.source(doc, XContentType.JSON);
        IndexResponse indexResponse = state.client.index(request, RequestOptions.DEFAULT);
        if (!indexResponse.status().toString().equals("CREATED")) {
            System.out.println("Index status is " + indexResponse.status());
        } else {

        }
    }

    private static void SearchDoc(
        MyState state,
        String indexName,
        String searchFieldName,
        String searchText,
        String sortFieldName,
        String highlightFieldName
    ) throws IOException {
        // Refresh the index before searching
        RefreshRequest refreshRequest = new RefreshRequest(indexName);
        RefreshResponse refreshResponse = state.client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
        if (!refreshResponse.getStatus().toString().equals("OK")) {
            System.out.println("refreshResponse: " + refreshResponse.getStatus());
        }

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery(searchFieldName, searchText));
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.sort(sortFieldName, SortOrder.DESC);
        sourceBuilder.highlighter(new HighlightBuilder().field(highlightFieldName));
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        SearchResponse SearchResponse = state.client.search(searchRequest, RequestOptions.DEFAULT);
        if (!SearchResponse.status().toString().equals("OK")) {
            System.out.println("the number of hit is: " + SearchResponse.getHits().getTotalHits().value);
            System.out.println("SearchResponse: " + SearchResponse.toString());
        }

        SearchHits hits = SearchResponse.getHits();
        long totalHits = hits.getTotalHits().value;
        if (totalHits == 0) {
            throw new IOException("No hit is found");
        }
    }

    private static String GenerateRandomJson() {
        JSONObject json = new JSONObject();
        Random random = new Random();

        // Create 100 nested levels
        for (int i = 0; i < 100; i++) {
            JSONObject nestedObject = new JSONObject();

            // Add 10 fields to each nested level
            for (int j = 0; j < 10; j++) {
                String field = "field" + i + "." + j;
                String value = generateRandomString(random);
                nestedObject.put(field, value);
            }

            // Add the nested object to the parent object

            String nestedObjectName = "nested" + i;
            json.put(nestedObjectName, nestedObject);
        }

        // Return the JSON document as a string
        JSONObject returnJson = new JSONObject();
        returnJson.put("nested0", json);
        // System.out.println("createdRandomJSONString is: " + returnJson.toString());
        return returnJson.toString();
    }

    private static String generateRandomString(Random random) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int length = 10;
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomString.append(alphabet.charAt(random.nextInt(alphabet.length())));
            randomString.append(random.nextInt(10));
        }
        return randomString.toString();
    }

    private static Map<String, String> findNestedValueAndPath(String randomJsonString, int levelNumber, String currentPath) {
        JSONObject jsonObject = new JSONObject(randomJsonString);
        String targetKey = "field" + levelNumber;
        Map<String, String> result = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String path = currentPath + "." + key;
            Object value = jsonObject.get(key);
            if (key.equals(targetKey)) {
                result.put("value", value.toString());
                result.put("path", path);
                return result;
            }
            if (value instanceof JSONObject) {
                Map<String, String> nestedResult = findNestedValueAndPath(value.toString(), levelNumber, path);
                if (!nestedResult.isEmpty()) {
                    return nestedResult;
                }
            }
        }
        return result;
    }

    private static String getNestedValue(String randomJsonString, int levelNumber) {
        // Parse the nested JSON document string into a JSONObject
        JSONObject json = new JSONObject(randomJsonString);
        // Get the value of the "field" in levelNumber
        for (int i = 0; i < levelNumber; i++) {
            json = json.getJSONObject("field" + levelNumber);
        }

        return json.getString("value");
    }

    private static Map<String, Object> findValueAndPath(JSONObject jsonObject, String targetValue, String currentPath) {
        Map<String, Object> result = new HashMap<>();

        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            String path = currentPath + "/" + key;

            if (value instanceof JSONObject) {
                Map<String, Object> innerResult = findValueAndPath((JSONObject) value, targetValue, path);

                if (!innerResult.isEmpty()) {
                    result.putAll(innerResult);
                    break;
                }
            } else if (value instanceof String && value.equals(targetValue)) {
                result.put("value", value);
                result.put("path", path);
                break;
            }
        }

        return result;
    }

}
