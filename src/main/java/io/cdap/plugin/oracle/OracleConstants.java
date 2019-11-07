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

/**
 * Oracle constants.
 */
public class OracleConstants {

  private OracleConstants() {
    throw new AssertionError("Should not instantiate static utility class.");
  }

  /**
   * Oracle plugin name.
   */
  public static final String PLUGIN_NAME = "Oracle";

  /**
   * Configuration property name used to specify the mode of authentication to use.
   */
  public static final String AUTHENTICATION_TYPE = "authenticationType";

  /**
   * Configuration property name used to specify REST server URL from the account creation email received from Oracle.
   */
  public static final String SERVER_URL = "serverUrl";

  /**
   * Configuration property name used to specify Oracle Service Cloud username.
   */
  public static final String USERNAME = "username";

  /**
   * Configuration property name used to specify Oracle Service Cloud password.
   */
  public static final String PASSWORD = "password";

  /**
   * Configuration property name used to specify the schema of the entries.
   */
  public static final String SCHEMA = "schema";

  /**
   * Configuration property name used to specify session ID for Oracle Service Cloud.
   */
  public static final String SESSION_ID = "sessionId";

  /**
   * Configuration property name used to specify OAuth access token, generated by an external identity provider.
   */
  public static final String ACCESS_TOKEN = "accessToken";

  /**
   * Configuration property name used to specify the type of query.
   */
  public static final String QUERY_TYPE = "queryType";

  /**
   * Configuration property name used to specify an object to pull from Oracle Service Cloud. Examples of objects are
   * Accounts, Analytics Reports, Opportunities, etc.
   */
  public static final String SERVICE_CLOUD_OBJECT = "serviceCloudObject";

  /**
   * Configuration property name used to specify ROQL query that specifies the data to import.
   */
  public static final String QUERY = "query";

  /**
   * Configuration property name used to specify field in the selected object to sort the results by.
   */
  public static final String SORT_BY = "sortBy";

  /**
   * Configuration property name used to specify the sort direction: Ascending or Descending.
   */
  public static final String SORT_DIRECTION = "sortDirection";

  /**
   * Configuration property name used to filter data to include only records which have modification date greater than
   * or equal to the specified date.
   */
  public static final String START_DATE = "startDate";

  /**
   * Configuration property name used to filter data to include only records which have modification date less than the
   * specified date.
   */
  public static final String END_DATE = "endDate";
}