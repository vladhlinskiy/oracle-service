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

import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.oracle.schema.AccountsSchema;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Specifies the Oracle Service Cloud objects along with their display name and resource name.
 */
public enum OracleObject {
  ACCOUNT("Accounts", "accounts", AccountsSchema.SCHEMA);

  private static final Map<String, OracleObject> byDisplayName = Arrays.stream(values())
    .collect(Collectors.toMap(OracleObject::getDisplayName, Function.identity()));

  private final String displayName;
  private final String resourceName;
  private final Schema schema;

  OracleObject(String displayName, String resourceName, Schema schema) {
    this.displayName = displayName;
    this.resourceName = resourceName;
    this.schema = schema;
  }

  @Nullable
  public static OracleObject fromDisplayName(String displayName) {
    return byDisplayName.get(displayName);
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getResourceName() {
    return resourceName;
  }

  public Schema getSchema() {
    return schema;
  }
}
