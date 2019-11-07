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

import com.google.gson.JsonObject;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.oracle.OracleConfig;

/**
 * Transforms {@link JsonObject} to {@link StructuredRecord}.
 */
public class JsonObjectToRecordTransformer {

  private final OracleConfig config;
  private final Schema schema;

  public JsonObjectToRecordTransformer(OracleConfig config, Schema schema) {
    this.config = config;
    this.schema = schema;
  }

  /**
   * Transforms given {@link JsonObject} to {@link StructuredRecord}.
   *
   * @param jsonObject JSON object to be transformed.
   * @return {@link StructuredRecord} that corresponds to the given {@link JsonObject}.
   */
  public StructuredRecord transform(JsonObject jsonObject) {
    // TODO implement
    return null;
  }
}
