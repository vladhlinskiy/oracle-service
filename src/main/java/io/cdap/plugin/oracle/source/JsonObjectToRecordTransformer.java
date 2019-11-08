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

package io.cdap.plugin.oracle.source;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.format.UnexpectedFormatException;
import io.cdap.cdap.api.data.schema.Schema;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

/**
 * Transforms {@link JsonObject} to {@link StructuredRecord}.
 */
public class JsonObjectToRecordTransformer {

  private final Schema schema;

  public JsonObjectToRecordTransformer(Schema schema) {
    this.schema = schema;
  }

  /**
   * Transforms given {@link JsonObject} to {@link StructuredRecord}.
   *
   * @param jsonObject JSON object to be transformed.
   * @return {@link StructuredRecord} that corresponds to the given {@link JsonObject}.
   */
  public StructuredRecord transform(JsonObject jsonObject) {
    return extractRecord(null, jsonObject, schema);
  }

  private StructuredRecord extractRecord(@Nullable String fieldName, JsonObject object, Schema schema) {
    StructuredRecord.Builder builder = StructuredRecord.builder(schema);
    List<Schema.Field> fields = Objects.requireNonNull(schema.getFields(), "Schema fields cannot be empty");
    for (Schema.Field field : fields) {
      // Use full field name for nested records to construct meaningful errors messages.
      // Full field names will be in the following format: 'record_field_name.nested_record_field_name'
      String fullFieldName = fieldName != null ? String.format("%s.%s", fieldName, field.getName()) : field.getName();
      Schema nonNullableSchema = field.getSchema().isNullable() ? field.getSchema().getNonNullable()
        : field.getSchema();
      Object value = extractValue(fullFieldName, object.get(field.getName()), nonNullableSchema);
      builder.set(field.getName(), value);
    }
    return builder.build();
  }

  private Object extractValue(String fieldName, Object object, Schema schema) {
    if (object == null || object instanceof JsonNull) {
      return null;
    }

    Schema.LogicalType fieldLogicalType = schema.getLogicalType();
    if (fieldLogicalType != null) {
      switch (fieldLogicalType) {
        case DECIMAL:
          ensureTypeValid(fieldName, object, JsonPrimitive.class);
          ensurePrimitiveIsNumber(fieldName, ((JsonPrimitive) object));
          return extractDecimal(fieldName, ((JsonPrimitive) object).getAsBigDecimal(), schema);
        default:
          throw new UnexpectedFormatException(String.format("Field '%s' is of unsupported type '%s'", fieldName,
                                                            fieldLogicalType.name().toLowerCase()));
      }
    }

    Schema.Type fieldType = schema.getType();
    switch (fieldType) {
      case BOOLEAN:
        ensureTypeValid(fieldName, object, JsonPrimitive.class);
        ensurePrimitiveIsBoolean(fieldName, ((JsonPrimitive) object));
        return ((JsonPrimitive) object).getAsBoolean();
      case INT:
        ensureTypeValid(fieldName, object, JsonPrimitive.class);
        ensurePrimitiveIsNumber(fieldName, ((JsonPrimitive) object));
        return ((JsonPrimitive) object).getAsInt();
      case FLOAT:
        ensureTypeValid(fieldName, object, JsonPrimitive.class);
        ensurePrimitiveIsNumber(fieldName, ((JsonPrimitive) object));
        return ((JsonPrimitive) object).getAsFloat();
      case DOUBLE:
        ensureTypeValid(fieldName, object, JsonPrimitive.class);
        ensurePrimitiveIsNumber(fieldName, ((JsonPrimitive) object));
        return ((JsonPrimitive) object).getAsDouble();
      case LONG:
        ensureTypeValid(fieldName, object, JsonPrimitive.class);
        ensurePrimitiveIsNumber(fieldName, ((JsonPrimitive) object));
        return ((JsonPrimitive) object).getAsLong();
      case STRING:
        ensureTypeValid(fieldName, object, JsonPrimitive.class);
        ensurePrimitiveIsString(fieldName, ((JsonPrimitive) object));
        return ((JsonPrimitive) object).getAsString();
      case MAP:
        ensureTypeValid(fieldName, object, JsonObject.class);
        Schema valueSchema = schema.getMapSchema().getValue();
        Schema nonNullableValueSchema = valueSchema.isNullable() ? valueSchema.getNonNullable() : valueSchema;
        return extractMap(fieldName, (JsonObject) object, nonNullableValueSchema);
      case ARRAY:
        ensureTypeValid(fieldName, object, JsonArray.class);
        Schema componentSchema = schema.getComponentSchema().isNullable() ? schema.getComponentSchema().getNonNullable()
          : schema.getComponentSchema();
        return extractArray(fieldName, (JsonArray) object, componentSchema);
      case RECORD:
        ensureTypeValid(fieldName, object, JsonObject.class);
        return extractRecord(fieldName, (JsonObject) object, schema);
      default:
        throw new UnexpectedFormatException(String.format("Field '%s' is of unsupported type '%s'", fieldName,
                                                          fieldType.name().toLowerCase()));
    }
  }

  private Map<String, Object> extractMap(String fieldName, JsonObject jsonObject, Schema valueSchema) {
    Map<String, Object> extracted = new HashMap<>();
    for (String key : jsonObject.keySet()) {
      Object value = jsonObject.get(key);
      extracted.put(key, extractValue(fieldName, value, valueSchema));
    }
    return extracted;
  }

  private List<Object> extractArray(String fieldName, JsonArray array, Schema componentSchema) {
    List<Object> values = Lists.newArrayListWithCapacity(array.size());
    for (Object obj : array) {
      values.add(extractValue(fieldName, obj, componentSchema));
    }
    return values;
  }

  private byte[] extractDecimal(String fieldName, BigDecimal decimal, Schema schema) {
    int schemaPrecision = schema.getPrecision();
    int schemaScale = schema.getScale();
    if (decimal.precision() > schemaPrecision) {
      throw new UnexpectedFormatException(
        String.format("Field '%s' has precision '%s' which is higher than schema precision '%s'.",
                      fieldName, decimal.precision(), schemaPrecision));
    }

    if (decimal.scale() > schemaScale) {
      throw new UnexpectedFormatException(
        String.format("Field '%s' has scale '%s' which is not equal to schema scale '%s'.",
                      fieldName, decimal.scale(), schemaScale));
    }

    return decimal.setScale(schemaScale).unscaledValue().toByteArray();
  }

  private void ensureTypeValid(String fieldName, Object value, Class... expectedTypes) {
    for (Class expectedType : expectedTypes) {
      if (expectedType.isInstance(value)) {
        return;
      }
    }

    String expectedTypeNames = Stream.of(expectedTypes)
      .map(Class::getName)
      .collect(Collectors.joining(", "));
    throw new UnexpectedFormatException(
      String.format("Document field '%s' is expected to be of type '%s', but found a '%s'.", fieldName,
                    expectedTypeNames, value.getClass().getSimpleName()));
  }

  private void ensurePrimitiveIsBoolean(String fieldName, JsonPrimitive primitive) {
    if (primitive.isBoolean()) {
      return;
    }

    throw new UnexpectedFormatException(
      String.format("Document field '%s' is expected to be a boolean, but found a '%s'.", fieldName, primitive));
  }

  private void ensurePrimitiveIsNumber(String fieldName, JsonPrimitive primitive) {
    if (primitive.isNumber()) {
      return;
    }

    throw new UnexpectedFormatException(
      String.format("Document field '%s' is expected to be a number, but found a '%s'.", fieldName, primitive));
  }

  private void ensurePrimitiveIsString(String fieldName, JsonPrimitive primitive) {
    if (primitive.isString()) {
      return;
    }

    throw new UnexpectedFormatException(
      String.format("Document field '%s' is expected to be a string, but found a '%s'.", fieldName, primitive));
  }
}
