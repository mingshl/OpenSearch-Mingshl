///*
// * SPDX-License-Identifier: Apache-2.0
// *
// * The OpenSearch Contributors require contributions made to
// * this file be licensed under the Apache-2.0 license or a
// * compatible open source license.
// */
//
//package org.opensearch.flatobject.xcontent;
//
//import org.apache.lucene.document.FieldType;
//import org.opensearch.common.bytes.BytesReference;
//import org.opensearch.common.xcontent.DeprecationHandler;
//import org.opensearch.common.xcontent.NamedXContentRegistry;
//import org.opensearch.common.xcontent.XContentBuilder;
//import org.opensearch.common.xcontent.XContentHelper;
//import org.opensearch.common.xcontent.XContentLocation;
//import org.opensearch.common.xcontent.XContentParser;
//import org.opensearch.common.xcontent.XContentType;
//import org.opensearch.common.xcontent.json.JsonXContent;
//import org.opensearch.common.xcontent.support.AbstractXContentParser;
//import org.opensearch.index.mapper.ParseContext;
//
//import java.io.IOException;
//import java.nio.CharBuffer;
//import java.util.logging.Logger;
//
//public class JsonToStringXContentParser extends AbstractXContentParser {
//    private final String fieldTypeName;
//    private final XContentParser pathParser;
//    private XContentParser parser;
//    private XContentBuilder builder = XContentBuilder.builder(JsonXContent.jsonXContent);
//    private ParseContext parseContext;
//
//    private NamedXContentRegistry xContentRegistry;
//
//    private DeprecationHandler deprecationHandler;
//    /**
//     * logging function
//     */
//
//    private static final Logger logger = Logger.getLogger((JsonToStringXContentParser.class.getName()));
//    private XContentBuilder builder1;
//
//    public JsonToStringXContentParser(
//        NamedXContentRegistry xContentRegistry,
//        DeprecationHandler deprecationHandler,
//        ParseContext parseContext,
//        String fieldTypeName) throws IOException {
//        super(xContentRegistry, deprecationHandler);
//        this.parseContext = parseContext;
//        this.deprecationHandler = deprecationHandler;
//        this.xContentRegistry = xContentRegistry;
//        this.parser = parseContext.parser();
//        this.pathParser = parseContext.parser();
//        this.fieldTypeName = fieldTypeName;
//    }
//
//    public XContentParser parseObject() throws IOException {
////        builder.startObject(this.fieldTypeName);
//        builder.startObject();
//        parseToken();
//        /** test nested string **/
//        builder.startObject("pathAndValue");
//        parseTokenToValueAndPathString();
//        builder.endObject();
//        /** test nested string **/
//        builder.endObject();
//        String jString = XContentHelper.convertToJson(BytesReference.bytes(builder), false, XContentType.JSON);
//        logger.info("Before createParser, jString: " + jString + "\n");
//
//        return JsonXContent.jsonXContent.createParser(this.xContentRegistry, this.deprecationHandler, String.valueOf(jString));
//    }
//
//    private void parseTokenToValueAndPathString() throws IOException {
//        String currentFieldName;
//        while (this.pathParser.nextToken() != Token.END_OBJECT) {
//
//            currentFieldName = this.pathParser.currentName();
//
//            logger.info("currentFieldName: " + currentFieldName + "\n");
//            StringBuilder parsedFields = new StringBuilder();
//            StringBuilder path = new StringBuilder(fieldTypeName);
//            if (this.pathParser.nextToken() == Token.START_OBJECT){
//                /**
//                 * for nested Json, make a copy of parser, then parse the entire Json as string.
//                 * for example:
//                 * {"grandpa": {
//                 *     "dad": {
//                 *     "son": "me"
//                 * } }
//                 * the JSON object would be read as ONE string field for "grandpa" would be
//                 *
//                 * grandpa: {
//                 * "grandpa_key": "grandpa",
//                 * "grandpa_value"= "{dad: {son: me}}} ,
//                 * "grandpa_pathAndValue =  "grandpa={"dad: {son: me}}}"
//                 * "dad_key" = "dad",
//                 * "dad_value"= "{son: me}",
//                 * "dad_pathAndValue =  "grandpa.dad={son: me}}"
//                 * "son_key" = "son",
//                 * "son_value"= "me",
//                 * "son_pathAndValue =  "grandpa.dad.son=me" }"
//                 *
//                 */
//                //To do. to convert the entire JsonObject without changing the tokenizer position.
//                //test nested string field, commented pathAndValue out
////                path.append(currentFieldName);
//                parsedFields.append(this.pathParser.toString() );
////              parsedFields.append(this.parser.mapOrdered().toString() );
////                builder.field(currentFieldName + "_key", currentFieldName);
////                builder.field(currentFieldName + "_value", parsedFields.toString());
//                //test nested string field, commented pathAndValue out
//                builder.field(currentFieldName + "_pathAndValue", path + "=" + parsedFields.toString());
//                parseTokenToValueAndPathString();
//            }
//            else{
//                path.append("."+currentFieldName);
//                parseValue(currentFieldName, parsedFields);
////                builder.field(currentFieldName + "_key", currentFieldName);
////                builder.field(currentFieldName + "_value", parsedFields.toString());
//                //test nested string field, commented pathAndValue out
//                builder.field(currentFieldName + "_pathAndValue", path + "=" + parsedFields.toString());
//            }
//
//        }
//
//    }
//
//    private void parseToken() throws IOException {
//        String currentFieldName;
//        while (this.parser.nextToken() != Token.END_OBJECT) {
//
//            currentFieldName = this.parser.currentName();
//
//            logger.info("currentFieldName: " + currentFieldName + "\n");
//            StringBuilder parsedFields = new StringBuilder();
////            StringBuilder path = new StringBuilder(fieldTypeName);
//            if (this.parser.nextToken() == Token.START_OBJECT){
//                /**
//                 * for nested Json, make a copy of parser, then parse the entire Json as string.
//                 * for example:
//                 * {"grandpa": {
//                 *     "dad": {
//                 *     "son": "me"
//                 * } }
//                 * the JSON object would be read as ONE string field for "grandpa" would be
//                 *
//                 * grandpa: {
//                 * "grandpa_key": "grandpa",
//                 * "grandpa_value"= "{dad: {son: me}}} ,
//                 * "grandpa_pathAndValue =  "grandpa={"dad: {son: me}}}"
//                 * "dad_key" = "dad",
//                 * "dad_value"= "{son: me}",
//                 * "dad_pathAndValue =  "grandpa.dad={son: me}}"
//                 * "son_key" = "son",
//                 * "son_value"= "me",
//                 * "son_pathAndValue =  "grandpa.dad.son=me" }"
//                 *
//                 */
//                //To do. to convert the entire JsonObject without changing the tokenizer position.
//                //test nested string field, commented pathAndValue out
////                path.append(currentFieldName);
//                parsedFields.append(this.parser.toString() );
////              parsedFields.append(this.parser.mapOrdered().toString() );
//                builder.field(currentFieldName + "_key", currentFieldName);
//                builder.field(currentFieldName + "_value", parsedFields.toString());
//                //test nested string field, commented pathAndValue out
////                builder.field(currentFieldName + "_pathAndValue", path + "=" + parsedFields.toString());
//                parseToken();
//            }
//            else{
////                path.append("."+currentFieldName);
//                parseValue(currentFieldName, parsedFields);
//                builder.field(currentFieldName + "_key", currentFieldName);
//                builder.field(currentFieldName + "_value", parsedFields.toString());
//                //test nested string field, commented pathAndValue out
////                builder.field(currentFieldName + "_pathAndValue", path + "=" + parsedFields.toString());
//            }
//
//        }
//    }
//
//    private void parseValue(String currentFieldName, StringBuilder parsedFields) throws IOException {
//        logger.info("this.parser.currentToken(): " + this.parser.currentToken() + "\n");
//        switch (this.parser.currentToken()) {
//            case VALUE_STRING:
//                /**
//                 * this is "value" only format for each subfield
//                 * parsedFields will contain {"key_path": "key", "key_value": "value"}
//                 */
//                parsedFields.append(this.parser.textOrNull());
//                logger.info("currentFieldName and parsedFields :" + currentFieldName + " " + parsedFields.toString() + "\n");
//                break;
//            // Handle other token types as needed
//            // ToDo, what do we do, if encountered these fields?
//                // should never get to START_OBJECT
//            case START_OBJECT:
//                throw new IOException("Unsupported token type");
//            case FIELD_NAME:
//                // should never get to FIELD_NAME
//                logger.info("token is FIELD_NAME: " + this.parser.currentName() + "\n");
//                break;
//            case  VALUE_EMBEDDED_OBJECT:
//                logger.info("token is VALUE_EMBEDDED_OBJECT: " + this.parser.objectText()+ "\n");
//                break;
//            default:
//                throw new IOException("Unsupported token type [" + parser.currentToken() + "]");
//        }
//    }
//
//    @Override
//    public XContentType contentType() {
//        return XContentType.JSON;
//    }
//
//    @Override
//    public Token nextToken() throws IOException {
//        return this.parser.nextToken();
//    }
//
//    @Override
//    public void skipChildren() throws IOException {
//        this.parser.skipChildren();
//    }
//
//    @Override
//    public Token currentToken() {
//        return this.parser.currentToken();
//    }
//
//    @Override
//    public String currentName() throws IOException {
//        return this.parser.currentName();
//    }
//
//    @Override
//    public String text() throws IOException {
//        return this.parser.text();
//    }
//
//    @Override
//    public CharBuffer charBuffer() throws IOException {
//        return this.parser.charBuffer();
//    }
//
//    @Override
//    public Object objectText() throws IOException {
//        return this.parser.objectText();
//    }
//
//    @Override
//    public Object objectBytes() throws IOException {
//        return this.parser.objectBytes();
//    }
//
//    @Override
//    public boolean hasTextCharacters() {
//        return this.parser.hasTextCharacters();
//    }
//
//    @Override
//    public char[] textCharacters() throws IOException {
//        return this.parser.textCharacters();
//    }
//
//    @Override
//    public int textLength() throws IOException {
//        return this.parser.textLength();
//    }
//
//    @Override
//    public int textOffset() throws IOException {
//        return this.parser.textOffset();
//    }
//
//    @Override
//    public Number numberValue() throws IOException {
//        return this.parser.numberValue();
//    }
//
//    @Override
//    public NumberType numberType() throws IOException {
//        return this.parser.numberType();
//    }
//
//    @Override
//    public byte[] binaryValue() throws IOException {
//        return this.parser.binaryValue();
//    }
//
//    @Override
//    public XContentLocation getTokenLocation() {
//        return this.parser.getTokenLocation();
//    }
//
//    @Override
//    protected boolean doBooleanValue() throws IOException {
//        return this.parser.booleanValue();
//    }
//
//    @Override
//    protected short doShortValue() throws IOException {
//        return this.parser.shortValue();
//    }
//
//    @Override
//    protected int doIntValue() throws IOException {
//        return this.parser.intValue();
//    }
//
//    @Override
//    protected long doLongValue() throws IOException {
//        return this.parser.longValue();
//    }
//
//    @Override
//    protected float doFloatValue() throws IOException {
//        return this.parser.floatValue();
//    }
//
//    @Override
//    protected double doDoubleValue() throws IOException {
//        return this.parser.doubleValue();
//    }
//
//    @Override
//    public boolean isClosed() {
//        return this.parser.isClosed();
//    }
//
//    @Override
//    public void close() throws IOException {
//        this.parser.close();
//    }
//}

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
import org.opensearch.common.xcontent.XContentLocation;
import org.opensearch.common.xcontent.XContentParser;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.common.xcontent.json.JsonXContent;
import org.opensearch.common.xcontent.support.AbstractXContentParser;
import org.opensearch.index.mapper.ParseContext;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

public class JsonToStringXContentParser extends AbstractXContentParser {
    private final String fieldTypeName;
    private XContentParser parser;

    private ArrayList<String> valueList = new ArrayList<>();
    private ArrayList<String> valueAndPathList = new ArrayList<>();
    private ArrayList<String> keyList = new ArrayList<>();

    private XContentBuilder builder = XContentBuilder.builder(JsonXContent.jsonXContent);
    private ParseContext parseContext;

    private NamedXContentRegistry xContentRegistry;

    private DeprecationHandler deprecationHandler;
    /**
     * logging function
     */

    private static final Logger logger = Logger.getLogger((JsonToStringXContentParser.class.getName()));

    public JsonToStringXContentParser(
        NamedXContentRegistry xContentRegistry,
        DeprecationHandler deprecationHandler,
        ParseContext parseContext,
        String fieldTypeName
    ) throws IOException {
        super(xContentRegistry, deprecationHandler);
        this.parseContext = parseContext;
        this.deprecationHandler = deprecationHandler;
        this.xContentRegistry = xContentRegistry;
        this.parser = parseContext.parser();
        this.fieldTypeName = fieldTypeName;
    }

    public XContentParser parseObject() throws IOException {
        builder.startObject();
        parseToken();
        builder.field(this.fieldTypeName, keyList);
        builder.field(this.fieldTypeName + "._value", valueList);
        builder.field(this.fieldTypeName + "._valueAndPath", valueAndPathList);
        builder.endObject();
        String jString = XContentHelper.convertToJson(BytesReference.bytes(builder), false, XContentType.JSON);
        logger.info("Before createParser, jString: " + jString + "\n");
        return JsonXContent.jsonXContent.createParser(this.xContentRegistry, this.deprecationHandler, String.valueOf(jString));
    }

    private void parseToken() throws IOException {
        String currentFieldName;
        while (this.parser.nextToken() != Token.END_OBJECT) {

            currentFieldName = this.parser.currentName();

            logger.info("currentFieldName: " + currentFieldName + "\n");
            StringBuilder parsedFields = new StringBuilder();
            StringBuilder path = new StringBuilder(fieldTypeName);
            if (this.parser.nextToken() == Token.START_OBJECT) {
                /**
                 * for nested Json, make a copy of parser, then parse the entire Json as string.
                 * for example:
                 * {"grandpa": {
                 *     "dad": {
                 *     "son": "me"
                 * } }
                 * the JSON object would be read as three string fields for "grandpa" would be
                 * grandpa: {"dad","son"} -- the parent string field contains the keys only.
                 * grandpa._value: { "{dad: {son: me}}} ,"{son: me}","me"} -- the _value sub string field contains the values only.
                 * grandpa._pathAndValue: {  "grandpa={"dad: {son: me}}}","grandpa.dad={son: me}}", "grandpa.dad.son=me"}
                 * -- the _pathAndValue sub string field contains the "path=Value" format.
                 */
                // TODO: to convert the entire JsonObject as string without changing the tokenizer position.
                path.append("." + currentFieldName);
                parsedFields.append(this.parser.toString());
                this.keyList.add(currentFieldName);
                this.valueList.add(parsedFields.toString());
                this.valueAndPathList.add(path + "=" + parsedFields.toString());
                parseToken();
            } else {
                path.append("." + currentFieldName);
                parseValue(currentFieldName, parsedFields);
                this.keyList.add(currentFieldName);
                this.valueList.add(parsedFields.toString());
                this.valueAndPathList.add(path + "=" + parsedFields.toString());
            }

        }
    }

    private void parseValue(String currentFieldName, StringBuilder parsedFields) throws IOException {
        logger.info("this.parser.currentToken(): " + this.parser.currentToken() + "\n");
        switch (this.parser.currentToken()) {
            case VALUE_STRING:
                parsedFields.append(this.parser.textOrNull());
                logger.info("currentFieldName and parsedFields :" + currentFieldName + " " + parsedFields.toString() + "\n");
                break;
            // Handle other token types as needed
            // ToDo, what do we do, if encountered these fields?
            // should never get to START_OBJECT
            case START_OBJECT:
                throw new IOException("Unsupported token type");
            case FIELD_NAME:
                // should never get to FIELD_NAME
                logger.info("token is FIELD_NAME: " + this.parser.currentName() + "\n");
                break;
            case VALUE_EMBEDDED_OBJECT:
                logger.info("token is VALUE_EMBEDDED_OBJECT: " + this.parser.objectText() + "\n");
                break;
            default:
                throw new IOException("Unsupported token type [" + parser.currentToken() + "]");
        }
    }

    @Override
    public XContentType contentType() {
        return XContentType.JSON;
    }

    @Override
    public Token nextToken() throws IOException {
        return this.parser.nextToken();
    }

    @Override
    public void skipChildren() throws IOException {
        this.parser.skipChildren();
    }

    @Override
    public Token currentToken() {
        return this.parser.currentToken();
    }

    @Override
    public String currentName() throws IOException {
        return this.parser.currentName();
    }

    @Override
    public String text() throws IOException {
        return this.parser.text();
    }

    @Override
    public CharBuffer charBuffer() throws IOException {
        return this.parser.charBuffer();
    }

    @Override
    public Object objectText() throws IOException {
        return this.parser.objectText();
    }

    @Override
    public Object objectBytes() throws IOException {
        return this.parser.objectBytes();
    }

    @Override
    public boolean hasTextCharacters() {
        return this.parser.hasTextCharacters();
    }

    @Override
    public char[] textCharacters() throws IOException {
        return this.parser.textCharacters();
    }

    @Override
    public int textLength() throws IOException {
        return this.parser.textLength();
    }

    @Override
    public int textOffset() throws IOException {
        return this.parser.textOffset();
    }

    @Override
    public Number numberValue() throws IOException {
        return this.parser.numberValue();
    }

    @Override
    public NumberType numberType() throws IOException {
        return this.parser.numberType();
    }

    @Override
    public byte[] binaryValue() throws IOException {
        return this.parser.binaryValue();
    }

    @Override
    public XContentLocation getTokenLocation() {
        return this.parser.getTokenLocation();
    }

    @Override
    protected boolean doBooleanValue() throws IOException {
        return this.parser.booleanValue();
    }

    @Override
    protected short doShortValue() throws IOException {
        return this.parser.shortValue();
    }

    @Override
    protected int doIntValue() throws IOException {
        return this.parser.intValue();
    }

    @Override
    protected long doLongValue() throws IOException {
        return this.parser.longValue();
    }

    @Override
    protected float doFloatValue() throws IOException {
        return this.parser.floatValue();
    }

    @Override
    protected double doDoubleValue() throws IOException {
        return this.parser.doubleValue();
    }

    @Override
    public boolean isClosed() {
        return this.parser.isClosed();
    }

    @Override
    public void close() throws IOException {
        this.parser.close();
    }
}
