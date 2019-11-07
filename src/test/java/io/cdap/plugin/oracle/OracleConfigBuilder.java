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

import javax.annotation.Nullable;

/**
 * Builder class that provides handy methods to construct {@link OracleConfig} for testing.
 */
public class OracleConfigBuilder {

  private String referenceName;
  private String authenticationTypeName;
  private String queryTypeName;
  private String serverUrl;

  @Nullable
  private String user;

  @Nullable
  private String password;

  @Nullable
  private String sessionId;

  @Nullable
  private String accessToken;

  @Nullable
  private String serviceCloudObject;

  @Nullable
  private String query;

  @Nullable
  private String sortBy;

  @Nullable
  private String sortDirection;

  @Nullable
  private String startDate;

  @Nullable
  private String endDate;

  public static OracleConfigBuilder builder() {
    return new OracleConfigBuilder();
  }

  public static OracleConfigBuilder builder(OracleConfig original) {
    return new OracleConfigBuilder()
      .setReferenceName(original.getReferenceName())
      .setAuthenticationTypeName(original.getAuthenticationTypeName())
      .setQueryTypeName(original.getQueryTypeName())
      .setServerUrl(original.getServerUrl())
      .setUser(original.getUser())
      .setPassword(original.getPassword())
      .setSessionId(original.getSessionId())
      .setAccessToken(original.getAccessToken())
      .setServiceCloudObject(original.getServiceCloudObject())
      .setQuery(original.getQuery())
      .setSortBy(original.getSortBy())
      .setSortDirection(original.getSortDirection())
      .setStartDate(original.getStartDate())
      .setEndDate(original.getEndDate());
  }

  public OracleConfigBuilder setReferenceName(String referenceName) {
    this.referenceName = referenceName;
    return this;
  }

  public OracleConfigBuilder setAuthenticationTypeName(String authenticationTypeName) {
    this.authenticationTypeName = authenticationTypeName;
    return this;
  }

  public OracleConfigBuilder setQueryTypeName(String queryTypeName) {
    this.queryTypeName = queryTypeName;
    return this;
  }

  public OracleConfigBuilder setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
    return this;
  }

  public OracleConfigBuilder setUser(@Nullable String user) {
    this.user = user;
    return this;
  }

  public OracleConfigBuilder setPassword(@Nullable String password) {
    this.password = password;
    return this;
  }

  public OracleConfigBuilder setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  public OracleConfigBuilder setAccessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public OracleConfigBuilder setServiceCloudObject(@Nullable String serviceCloudObject) {
    this.serviceCloudObject = serviceCloudObject;
    return this;
  }

  public OracleConfigBuilder setQuery(@Nullable String query) {
    this.query = query;
    return this;
  }

  public OracleConfigBuilder setSortBy(@Nullable String sortBy) {
    this.sortBy = sortBy;
    return this;
  }

  public OracleConfigBuilder setSortDirection(@Nullable String sortDirection) {
    this.sortDirection = sortDirection;
    return this;
  }

  public OracleConfigBuilder setStartDate(@Nullable String startDate) {
    this.startDate = startDate;
    return this;
  }

  public OracleConfigBuilder setEndDate(@Nullable String endDate) {
    this.endDate = endDate;
    return this;
  }

  public OracleConfig build() {
    return new OracleConfig(referenceName, authenticationTypeName, serverUrl, user, password, sessionId, accessToken,
                            queryTypeName, serviceCloudObject, query, sortBy, sortDirection, startDate, endDate);
  }
}
