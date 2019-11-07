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

import com.google.common.base.Strings;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.api.plugin.PluginConfig;
import io.cdap.cdap.etl.api.FailureCollector;
import io.cdap.plugin.common.Constants;
import io.cdap.plugin.common.IdUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

/**
 * Defines a {@link PluginConfig} that Oracle Source can use.
 */
public class OracleConfig extends PluginConfig {

  @Name(Constants.Reference.REFERENCE_NAME)
  @Description(Constants.Reference.REFERENCE_NAME_DESCRIPTION)
  private String referenceName;

  @Name(OracleConstants.AUTHENTICATION_TYPE)
  @Description("Specifies the mode of authentication to use.")
  @Macro
  private String authenticationType;

  @Name(OracleConstants.SERVER_URL)
  @Description("REST server URL from the account creation email that you receive from Oracle. " +
    "If you do not have this information, contact your Oracle Cloud Account administrator.")
  @Macro
  private String serverUrl;

  @Name(OracleConstants.USERNAME)
  @Description("Oracle Service Cloud username.")
  @Macro
  @Nullable
  private String user;

  @Name(OracleConstants.PASSWORD)
  @Description("Oracle Service Cloud password.")
  @Macro
  @Nullable
  private String password;

  @Name(OracleConstants.SESSION_ID)
  @Description("Session ID for Oracle Service Cloud.")
  @Macro
  @Nullable
  private String sessionId;

  @Name(OracleConstants.ACCESS_TOKEN)
  @Description("OAuth access token, generated by an external identity provider.")
  @Macro
  @Nullable
  private String accessToken;

  @Name(OracleConstants.QUERY_TYPE)
  @Description("Specifies the type of query.")
  @Macro
  private String queryTypeName;

  @Name(OracleConstants.SERVICE_CLOUD_OBJECT)
  @Description("Select an object to pull from Oracle Service Cloud.")
  @Macro
  @Nullable
  private String serviceCloudObject;

  @Name(OracleConstants.QUERY)
  @Description("ROQL query that specifies the data to import.")
  @Macro
  @Nullable
  private String query;

  @Name(OracleConstants.SORT_BY)
  @Description("Field in the selected object to sort the results by.")
  @Macro
  @Nullable
  private String sortBy;

  @Name(OracleConstants.SORT_DIRECTION)
  @Description("Determines the sort direction: Ascending or Descending.")
  @Macro
  @Nullable
  private String sortDirection;

  @Name(OracleConstants.START_DATE)
  @Description("Filter data to include only records which have modification date greater than or equal to the " +
    "specified date.")
  @Macro
  @Nullable
  private String startDate;

  @Name(OracleConstants.END_DATE)
  @Description("Filter data to include only records which have modification date less than the specified date.")
  @Macro
  @Nullable
  private String endDate;

  public OracleConfig(String referenceName, String authenticationType, String serverUrl, String user, String password,
                      String sessionId, String accessToken, String queryTypeName, String serviceCloudObject,
                      String query, String sortBy, String sortDirection, String startDate, String endDate) {
    this.referenceName = referenceName;
    this.authenticationType = authenticationType;
    this.serverUrl = serverUrl;
    this.user = user;
    this.password = password;
    this.sessionId = sessionId;
    this.accessToken = accessToken;
    this.queryTypeName = queryTypeName;
    this.serviceCloudObject = serviceCloudObject;
    this.query = query;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public String getReferenceName() {
    return referenceName;
  }

  public String getAuthenticationType() {
    return authenticationType;
  }

  public String getServerUrl() {
    return serverUrl;
  }

  @Nullable
  public String getUser() {
    return user;
  }

  @Nullable
  public String getPassword() {
    return password;
  }

  @Nullable
  public String getSessionId() {
    return sessionId;
  }

  @Nullable
  public String getAccessToken() {
    return accessToken;
  }

  public String getQueryTypeName() {
    return queryTypeName;
  }

  @Nullable
  public String getServiceCloudObject() {
    return serviceCloudObject;
  }

  @Nullable
  public String getQuery() {
    return query;
  }

  @Nullable
  public String getSortBy() {
    return sortBy;
  }

  @Nullable
  public String getSortDirection() {
    return sortDirection;
  }

  @Nullable
  public String getStartDate() {
    return startDate;
  }

  @Nullable
  public String getEndDate() {
    return endDate;
  }

  /**
   * Validates {@link OracleConfig} instance.
   *
   * @param collector failure collector.
   */
  public void validate(FailureCollector collector) {
    // TODO review properties
    if (Strings.isNullOrEmpty(referenceName)) {
      collector.addFailure("Reference name must be specified", null)
        .withConfigProperty(Constants.Reference.REFERENCE_NAME);
    } else {
      IdUtils.validateReferenceName(referenceName, collector);
    }
    if (!containsMacro(OracleConstants.USERNAME) && !containsMacro(OracleConstants.PASSWORD) &&
      Strings.isNullOrEmpty(user) && !Strings.isNullOrEmpty(password)) {
      collector.addFailure("Username must be specified", null)
        .withConfigProperty(OracleConstants.USERNAME);
    }
    if (!containsMacro(OracleConstants.USERNAME) && !containsMacro(OracleConstants.PASSWORD) &&
      !Strings.isNullOrEmpty(user) && Strings.isNullOrEmpty(password)) {
      collector.addFailure("Password must be specified", null)
        .withConfigProperty(OracleConstants.PASSWORD);
    }
  }

  /**
   * Validates given input/output schema according the the specified supported types. Fields of types
   * {@link Schema.Type#RECORD}, {@link Schema.Type#ARRAY}, {@link Schema.Type#MAP} will be validated recursively.
   *
   * @param schema                schema to validate.
   * @param supportedLogicalTypes set of supported logical types.
   * @param supportedTypes        set of supported types.
   * @param collector             failure collector.
   */
  public void validateSchema(Schema schema, Set<Schema.LogicalType> supportedLogicalTypes,
                             Set<Schema.Type> supportedTypes, FailureCollector collector) {
    validateRecordSchema(schema, supportedLogicalTypes, supportedTypes, collector);
  }

  private void validateRecordSchema(Schema schema, Set<Schema.LogicalType> supportedLogicalTypes,
                                    Set<Schema.Type> supportedTypes, FailureCollector collector) {
    if (schema == null) {
      collector.addFailure("Schema must be specified", null)
        .withConfigProperty(OracleConstants.SCHEMA);
      return;
    }
    List<Schema.Field> fields = schema.getFields();
    if (fields == null || fields.isEmpty()) {
      collector.addFailure("Schema must contain at least one field", null)
        .withConfigProperty(OracleConstants.SCHEMA);
      return;
    }
    for (Schema.Field field : fields) {
      validateFieldSchema(field.getName(), field.getSchema(), supportedLogicalTypes, supportedTypes, collector);
    }
  }

  private void validateFieldSchema(String fieldName, Schema schema, Set<Schema.LogicalType> supportedLogicalTypes,
                                   Set<Schema.Type> supportedTypes, FailureCollector collector) {
    Schema nonNullableSchema = schema.isNullable() ? schema.getNonNullable() : schema;
    Schema.Type type = nonNullableSchema.getType();
    switch (type) {
      case RECORD:
        validateRecordSchema(nonNullableSchema, supportedLogicalTypes, supportedTypes, collector);
        break;
      case ARRAY:
        validateArraySchema(fieldName, nonNullableSchema, supportedLogicalTypes, supportedTypes, collector);
        break;
      case MAP:
        validateMapSchema(fieldName, nonNullableSchema, supportedLogicalTypes, supportedTypes, collector);
        break;
      default:
        validateSchemaType(fieldName, nonNullableSchema, supportedLogicalTypes, supportedTypes, collector);
    }
  }

  private void validateMapSchema(String fieldName, Schema schema, Set<Schema.LogicalType> supportedLogicalTypes,
                                 Set<Schema.Type> supportedTypes, FailureCollector collector) {
    Schema keySchema = schema.getMapSchema().getKey();
    if (keySchema.isNullable() || keySchema.getType() != Schema.Type.STRING) {
      collector.addFailure("Map keys must be a non-nullable string",
                           String.format("Change field '%s' to use a non-nullable string as the map key", fieldName))
        .withOutputSchemaField(fieldName, null);
    }
    validateFieldSchema(fieldName, schema.getMapSchema().getValue(), supportedLogicalTypes, supportedTypes, collector);
  }

  private void validateArraySchema(String fieldName, Schema schema, Set<Schema.LogicalType> supportedLogicalTypes,
                                   Set<Schema.Type> supportedTypes, FailureCollector collector) {
    Schema componentSchema = schema.getComponentSchema().isNullable() ? schema.getComponentSchema().getNonNullable()
      : schema.getComponentSchema();
    validateFieldSchema(fieldName, componentSchema, supportedLogicalTypes, supportedTypes, collector);
  }

  private void validateSchemaType(String fieldName, Schema fieldSchema, Set<Schema.LogicalType> supportedLogicalTypes,
                                  Set<Schema.Type> supportedTypes, FailureCollector collector) {
    Schema.Type type = fieldSchema.getType();
    Schema.LogicalType logicalType = fieldSchema.getLogicalType();
    if (supportedTypes.contains(type) || supportedLogicalTypes.contains(logicalType)) {
      return;
    }

    String supportedTypeNames = Stream.concat(
      supportedTypes.stream().map(Enum::name).map(String::toLowerCase),
      supportedLogicalTypes.stream().map(Schema.LogicalType::getToken)
    ).collect(Collectors.joining(", "));

    String errorMessage = String.format("Field '%s' is of unsupported type '%s'. Supported types are: %s",
                                        fieldName, fieldSchema.getDisplayName(), supportedTypeNames);
    collector.addFailure(errorMessage, String.format("Change field '%s' to be a supported type", fieldName))
      .withOutputSchemaField(fieldName, null);
  }
}
