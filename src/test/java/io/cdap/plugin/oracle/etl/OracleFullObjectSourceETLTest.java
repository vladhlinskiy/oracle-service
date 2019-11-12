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
package io.cdap.plugin.oracle.etl;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.oracle.AuthenticationType;
import io.cdap.plugin.oracle.OracleConstants;
import io.cdap.plugin.oracle.OracleObject;
import io.cdap.plugin.oracle.QueryType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class OracleFullObjectSourceETLTest extends BaseOracleSourceETLTest {

  private static final JsonParser PARSER = new JsonParser();
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private Map<Integer, JsonObject> accountsExpected;

  @Before
  public void testSetup() throws Exception {
    String resourceName = OracleObject.ACCOUNT.getResourceName();
    String accountsJson = readResourceFile(resourceName + ".json");
    JsonObject accounts = PARSER.parse(accountsJson).getAsJsonObject();
    accountsExpected = new HashMap<>();
    accounts.getAsJsonArray(OracleConstants.ITEMS).forEach(jsonElement -> {
      JsonObject account = jsonElement.getAsJsonObject();
      accountsExpected.put(account.get("id").getAsInt(), account);
    });
    wireMockRule.stubFor(WireMock.get(WireMock.urlEqualTo(OracleConstants.API_ROOT_PATH + resourceName))
                           .willReturn(WireMock.aResponse().withBody(accountsJson)));
  }

  @Test
  public void testSourceAccounts() throws Exception {
    Map<String, String> properties = sourceProperties(OracleObject.ACCOUNT.getDisplayName());
    List<StructuredRecord> records = getPipelineResults(properties);
    Assert.assertEquals(3, records.size());
    for (StructuredRecord actual : records) {
      Integer id = actual.<Integer>get("id");
      Assert.assertNotNull(id);
      assertEquals(accountsExpected.get(id), actual);
    }
  }

  @Test
  public void testSourceAccountsWithSchemaSet() throws Exception {
    String objectToPull = OracleObject.ACCOUNT.getDisplayName();
    Schema schema = OracleObject.ACCOUNT.getSchema();
    Map<String, String> properties = sourceProperties(objectToPull, schema);
    List<StructuredRecord> records = getPipelineResults(properties);
    Assert.assertEquals(3, records.size());
    for (StructuredRecord actual : records) {
      Integer id = actual.<Integer>get("id");
      Assert.assertNotNull(id);
      assertEquals(accountsExpected.get(id), actual);
    }
  }

  private void assertEquals(JsonObject expected, StructuredRecord actual) {
    Assert.assertEquals(expected.get("lookupName").getAsString(), actual.<String>get("lookupName"));
    // Assert account hierarchies equal
    assertLinksEqual(expected.get("accountHierarchy").getAsJsonObject(), actual.get("accountHierarchy"));
    // Assert account attributes equal
    JsonObject expectedAttributes = expected.get("attributes").getAsJsonObject();
    StructuredRecord actualAttributes = actual.get("attributes");
    Assert.assertEquals(expectedAttributes.get("accountLocked").getAsBoolean(), actualAttributes.get("accountLocked"));
    Assert.assertEquals(expectedAttributes.get("canModifyEmailSignature").getAsBoolean(),
                        actualAttributes.get("canModifyEmailSignature"));
    Assert.assertEquals(expectedAttributes.get("forcePasswordChange").getAsBoolean(),
                        actualAttributes.get("forcePasswordChange"));
    Assert.assertEquals(expectedAttributes.get("infrequentUser").getAsBoolean(),
                        actualAttributes.get("infrequentUser"));
    Assert.assertEquals(expectedAttributes.get("passwordNeverExpires").getAsBoolean(),
                        actualAttributes.get("passwordNeverExpires"));
    Assert.assertEquals(expectedAttributes.get("permanentlyDisabled").getAsBoolean(),
                        actualAttributes.get("permanentlyDisabled"));
    Assert.assertEquals(expectedAttributes.get("staffAssignmentDisabled").getAsBoolean(),
                        actualAttributes.get("staffAssignmentDisabled"));
    Assert.assertEquals(expectedAttributes.get("viewsReportsDisabled").getAsBoolean(),
                        actualAttributes.get("viewsReportsDisabled"));
    Assert.assertEquals(expectedAttributes.get("virtualAccount").getAsBoolean(),
                        actualAttributes.get("virtualAccount"));

    // Assert account countries equal
    assertLinksEqual(expected.get("country").getAsJsonObject(), actual.get("country"));

    Assert.assertEquals(expected.get("displayName").getAsString(), actual.<String>get("displayName"));
    Assert.assertNull(actual.get("emailNotification"));
    // Assert account emails equal
    assertLinksEqual(expected.get("emails").getAsJsonObject(), actual.get("emails"));

    Assert.assertEquals(expected.get("login").getAsString(), actual.<String>get("login"));
    Assert.assertNull(actual.get("manager"));

    JsonObject expectedName = expected.get("name").getAsJsonObject();
    StructuredRecord actualName = actual.get("name");
    Assert.assertEquals(expectedName.get("first").getAsString(), actualName.get("first"));
    Assert.assertEquals(expectedName.get("last").getAsString(), actualName.get("last"));

    StructuredRecord actualNameFurigana = actual.get("nameFurigana");
    Assert.assertNull(actualNameFurigana.get("first"));
    Assert.assertNull(actualNameFurigana.get("last"));

    Assert.assertEquals(expected.get("notificationPending").getAsBoolean(), actual.<Boolean>get("notificationPending"));
    Assert.assertEquals(expected.get("passwordExpirationTime").getAsString(), actual.get("passwordExpirationTime"));

    // Assert account phones equal
    assertLinksEqual(expected.get("phones").getAsJsonObject(), actual.get("phones"));
    assertRefsEqual(expected.get("profile").getAsJsonObject(), actual.get("profile"));
    JsonObject expectedSalesSettings = expected.get("salesSettings").getAsJsonObject();
    StructuredRecord actualSalesSettings = actual.get("salesSettings");
    assertRefsEqual(expectedSalesSettings.get("defaultCurrency").getAsJsonObject(),
                    actualSalesSettings.get("defaultCurrency"));
    Assert.assertNull(actualSalesSettings.get("territory"));

    StructuredRecord actualServiceSettings = actual.get("serviceSettings");
    Assert.assertNull(actualServiceSettings.get("screenPopPort"));
    Assert.assertNull(actual.get("signature"));
    Assert.assertNull(actual.get("staffGroup"));
  }

  private void assertLinksEqual(JsonObject expectedLinked, StructuredRecord actualLinked) {
    JsonArray expectedLinks = expectedLinked.get("links").getAsJsonArray();
    List<StructuredRecord> actualLinks = actualLinked.get("links");
    for (int i = 0; i < expectedLinks.size(); i++) {
      JsonObject link = expectedLinks.get(i).getAsJsonObject();
      StructuredRecord actualLink = actualLinks.get(i);
      Assert.assertEquals(link.get("rel").getAsString(), actualLink.get("rel"));
      Assert.assertEquals(link.get("href").getAsString(), actualLink.get("href"));
    }
  }

  private void assertRefsEqual(JsonObject expectedReference, StructuredRecord actualReference) {
    Assert.assertEquals(expectedReference.get("id").getAsInt(), (int) actualReference.get("id"));
    Assert.assertEquals(expectedReference.get("lookupName").getAsString(), actualReference.get("lookupName"));
  }

  private Map<String, String> sourceProperties(String objectToPull) {
    return sourceProperties(objectToPull, null);
  }

  private Map<String, String> sourceProperties(String objectToPull, @Nullable Schema schema) {
    return new ImmutableMap.Builder<String, String>()
      .put(OracleConstants.SERVER_URL, getServerAddress())
      .put(OracleConstants.AUTHENTICATION_TYPE, AuthenticationType.BASIC.getDisplayName())
      .put(OracleConstants.USERNAME, USERNAME)
      .put(OracleConstants.PASSWORD, PASSWORD)
      .put(OracleConstants.QUERY_TYPE, QueryType.FULL_OBJECT.getDisplayName())
      .put(OracleConstants.SERVICE_CLOUD_OBJECT, objectToPull)
      .put(OracleConstants.SCHEMA, schema == null ? "" : schema.toString())
      .build();
  }
}
