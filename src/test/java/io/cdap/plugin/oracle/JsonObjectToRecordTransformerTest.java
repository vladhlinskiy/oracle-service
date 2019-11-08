/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.oracle;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.format.UnexpectedFormatException;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.oracle.source.JsonObjectToRecordTransformer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * {@link JsonObjectToRecordTransformer} test.
 */
public class JsonObjectToRecordTransformerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  @SuppressWarnings("ConstantConditions")
  public void testTransform() {
    Schema nestedRecordSchema = Schema.recordOf("nested_object",
                                                Schema.Field.of("nested_string_field", Schema.of(Schema.Type.STRING)),
                                                Schema.Field.of("nested_long_field", Schema.of(Schema.Type.LONG)));

    Schema schema = Schema.recordOf("schema",
                                    Schema.Field.of("int_field", Schema.nullableOf(Schema.of(Schema.Type.INT))),
                                    Schema.Field.of("long_field", Schema.nullableOf(Schema.of(Schema.Type.LONG))),
                                    Schema.Field.of("float_field", Schema.nullableOf(Schema.of(Schema.Type.FLOAT))),
                                    Schema.Field.of("double_field", Schema.nullableOf(Schema.of(Schema.Type.DOUBLE))),
                                    Schema.Field.of("string_field", Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                                    Schema.Field.of("boolean_field", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
                                    Schema.Field.of("null_field", Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                                    Schema.Field.of("array_field",
                                                    Schema.arrayOf(Schema.nullableOf(Schema.of(Schema.Type.LONG)))),
                                    Schema.Field.of("nested_object", nestedRecordSchema),
                                    Schema.Field.of("object_to_map", Schema.mapOf(
                                      Schema.of(Schema.Type.STRING), Schema.of(Schema.Type.STRING))));

    StructuredRecord nestedRecord = StructuredRecord.builder(nestedRecordSchema)
      .set("nested_string_field", "some")
      .set("nested_long_field", Long.MAX_VALUE)
      .build();

    StructuredRecord expected = StructuredRecord.builder(schema)
      .set("int_field", 15)
      .set("long_field", 10L)
      .set("float_field", Float.MAX_VALUE)
      .set("double_field", 10.5D)
      .set("string_field", "string_value")
      .set("boolean_field", true)
      .set("null_field", null)
      .set("array_field", Arrays.asList(1L, null, 2L, null, 3L))
      .set("nested_object", nestedRecord)
      .set("object_to_map", ImmutableMap.<String, String>builder()
        .put("key", "value")
        .build()
      )
      .build();

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(1L);
    jsonArray.add(JsonNull.INSTANCE);
    jsonArray.add(2L);
    jsonArray.add(JsonNull.INSTANCE);
    jsonArray.add(3L);

    JsonObject nestedJsonObject = new JsonObject();
    nestedJsonObject.addProperty("nested_string_field", nestedRecord.<String>get("nested_string_field"));
    nestedJsonObject.addProperty("nested_long_field", nestedRecord.<Long>get("nested_long_field"));

    JsonObject mapObject = new JsonObject();
    mapObject.addProperty("key", "value");

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("int_field", expected.<Integer>get("int_field"));
    jsonObject.addProperty("long_field", expected.<Long>get("long_field"));
    jsonObject.addProperty("float_field", expected.<Float>get("float_field"));
    jsonObject.addProperty("double_field", expected.<Double>get("double_field"));
    jsonObject.addProperty("string_field", expected.<String>get("string_field"));
    jsonObject.addProperty("boolean_field", expected.<Boolean>get("boolean_field"));
    jsonObject.add("null_field", JsonNull.INSTANCE);
    jsonObject.add("array_field", jsonArray);
    jsonObject.add("nested_object", nestedJsonObject);
    jsonObject.add("object_to_map", mapObject);

    JsonObjectToRecordTransformer transformer = new JsonObjectToRecordTransformer(schema);
    StructuredRecord transformed = transformer.transform(jsonObject);

    Assert.assertEquals(expected, transformed);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  public void testTransformEmptyObject() {
    Schema nestedRecordSchema = Schema.recordOf("nested_object",
                                                Schema.Field.of("nested_string_field", Schema.of(Schema.Type.STRING)),
                                                Schema.Field.of("nested_long_field", Schema.of(Schema.Type.LONG)));

    Schema schema = Schema.recordOf("schema",
                                    Schema.Field.of("int_field", Schema.nullableOf(Schema.of(Schema.Type.INT))),
                                    Schema.Field.of("long_field", Schema.nullableOf(Schema.of(Schema.Type.LONG))),
                                    Schema.Field.of("double_field", Schema.nullableOf(Schema.of(Schema.Type.DOUBLE))),
                                    Schema.Field.of("string_field", Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                                    Schema.Field.of("boolean_field", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
                                    Schema.Field.of("bytes_field", Schema.nullableOf(Schema.of(Schema.Type.BYTES))),
                                    Schema.Field.of("null_field", Schema.nullableOf(Schema.of(Schema.Type.STRING))),
                                    Schema.Field.of("array_field", Schema.nullableOf(
                                      Schema.arrayOf(Schema.nullableOf(Schema.of(Schema.Type.LONG))))),
                                    Schema.Field.of("nested_object", Schema.nullableOf(nestedRecordSchema)),
                                    Schema.Field.of("object_to_map", Schema.nullableOf(
                                      Schema.mapOf(Schema.of(Schema.Type.STRING), Schema.of(Schema.Type.STRING)))));

    JsonObject jsonObject = new JsonObject();
    JsonObjectToRecordTransformer transformer = new JsonObjectToRecordTransformer(schema);
    StructuredRecord transformed = transformer.transform(jsonObject);
    Assert.assertNull(transformed.get("int_field"));
    Assert.assertNull(transformed.get("long_field"));
    Assert.assertNull(transformed.get("double_field"));
    Assert.assertNull(transformed.get("string_field"));
    Assert.assertNull(transformed.get("boolean_field"));
    Assert.assertNull(transformed.get("bytes_field"));
    Assert.assertNull(jsonObject.get("null_field"));
    Assert.assertNull(transformed.get("array_field"));
    Assert.assertNull(transformed.get("nested_object"));
    Assert.assertNull(transformed.get("object_to_map"));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  public void testTransformComplexMap() {
    Schema nestedRecordSchema = Schema.recordOf("nested_object",
                                                Schema.Field.of("nested_string_field", Schema.of(Schema.Type.STRING)),
                                                Schema.Field.of("nested_long_field", Schema.of(Schema.Type.LONG)));

    Schema schema = Schema.recordOf("schema", Schema.Field.of("object_to_map", Schema.mapOf(
      Schema.of(Schema.Type.STRING), nestedRecordSchema)));

    StructuredRecord nestedRecord = StructuredRecord.builder(nestedRecordSchema)
      .set("nested_string_field", "some")
      .set("nested_long_field", Long.MAX_VALUE)
      .build();

    StructuredRecord expected = StructuredRecord.builder(schema)
      .set("object_to_map", ImmutableMap.<String, StructuredRecord>builder()
        .put("key", nestedRecord)
        .build()
      )
      .build();

    JsonObject nestedJsonObject = new JsonObject();
    nestedJsonObject.addProperty("nested_string_field", nestedRecord.<String>get("nested_string_field"));
    nestedJsonObject.addProperty("nested_long_field", nestedRecord.<Long>get("nested_long_field"));
    JsonObject mapObject = new JsonObject();
    mapObject.add("key", nestedJsonObject);

    JsonObject jsonObject = new JsonObject();
    jsonObject.add("object_to_map", mapObject);

    JsonObjectToRecordTransformer transformer = new JsonObjectToRecordTransformer(schema);
    StructuredRecord transformed = transformer.transform(jsonObject);

    Assert.assertEquals(expected, transformed);
  }

  @Test
  public void testTransformUnexpectedFormat() {
    Schema schema = Schema.recordOf(
      "schema",
      Schema.Field.of("union_field", Schema.unionOf(Schema.of(Schema.Type.LONG), Schema.of(Schema.Type.STRING))));

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("union_field", 2019L);

    thrown.expect(UnexpectedFormatException.class);
    JsonObjectToRecordTransformer transformer = new JsonObjectToRecordTransformer(schema);
    transformer.transform(jsonObject);
  }

  @Test
  public void testTransformInvalidMapValue() {
    Schema schema = Schema.recordOf(
      "schema",
      Schema.Field.of("field", Schema.mapOf(Schema.of(Schema.Type.STRING), Schema.of(Schema.Type.STRING))));

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("field", "value");

    thrown.expect(UnexpectedFormatException.class);
    thrown.expectMessage("is expected to be of type");
    JsonObjectToRecordTransformer transformer = new JsonObjectToRecordTransformer(schema);
    transformer.transform(jsonObject);
  }

  @Test
  public void testTransformInvalidArrayValue() {
    Schema schema = Schema.recordOf("schema", Schema.Field.of("field", Schema.of(Schema.Type.INT)));

    JsonObject jsonObject = new JsonObject();
    jsonObject.add("field", new JsonObject());

    thrown.expect(UnexpectedFormatException.class);
    thrown.expectMessage("is expected to be of type");
    JsonObjectToRecordTransformer transformer = new JsonObjectToRecordTransformer(schema);
    transformer.transform(jsonObject);
  }


  @Test
  @SuppressWarnings("ConstantConditions")
  public void testTransformLogicalTypes() {
    Schema schema = Schema.recordOf("schema",
                                    Schema.Field.of("decimal_6_4", Schema.decimalOf(6, 4)),
                                    Schema.Field.of("decimal_34_4", Schema.decimalOf(34, 4)),
                                    Schema.Field.of("decimal_34_6", Schema.decimalOf(34, 6)));

    StructuredRecord expected = StructuredRecord.builder(schema)
      .setDecimal("decimal_6_4", new BigDecimal("10.1234"))
      .setDecimal("decimal_34_4", new BigDecimal("10.1234"))
      .setDecimal("decimal_34_6", new BigDecimal("10.123400"))
      .build();

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("decimal_6_4", new BigDecimal("10.1234"));
    jsonObject.addProperty("decimal_34_4", new BigDecimal("10.1234"));
    jsonObject.addProperty("decimal_34_6", new BigDecimal("10.1234"));

    JsonObjectToRecordTransformer transformer = new JsonObjectToRecordTransformer(schema);
    StructuredRecord transformed = transformer.transform(jsonObject);

    Assert.assertNotNull(transformed);
    Assert.assertEquals(expected.getDecimal("decimal_6_4"), transformed.getDecimal("decimal_6_4"));
    Assert.assertEquals(expected.getDecimal("decimal_34_4"), transformed.getDecimal("decimal_34_4"));
    Assert.assertEquals(expected.getDecimal("decimal_34_6"), transformed.getDecimal("decimal_34_6"));
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  public void testTransformDecimalNotRounded() {
    Schema schema = Schema.recordOf("schema", Schema.Field.of("decimal_34_2", Schema.decimalOf(34, 2)));
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("decimal_34_2", new BigDecimal("10.1234"));

    thrown.expectMessage("has scale '4' which is not equal to schema scale '2'");
    thrown.expect(UnexpectedFormatException.class);

    JsonObjectToRecordTransformer transformer = new JsonObjectToRecordTransformer(schema);
    transformer.transform(jsonObject);
  }
}
