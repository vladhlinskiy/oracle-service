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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class OracleFullObjectSourceETLTestBase extends BaseOracleSourceETLTest {

  private static final JsonParser PARSER = new JsonParser();
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private Map<Integer, JsonObject> expected;

  @Before
  public void testSetup() throws Exception {
    expected = getExpected();
    for (OracleObject oracleObject : OracleObject.values()) {
      String resourceName = oracleObject.getResourceName();
      wireMockRule.stubFor(WireMock.get(WireMock.urlMatching(OracleConstants.API_ROOT_PATH + resourceName + ".*"))
                             .willReturn(WireMock.aResponse().withBody(readResourceFile(resourceName + ".json"))));
    }
  }

  public Map<Integer, JsonObject> getExpected() throws IOException, URISyntaxException {
    String resourceName = getObjectToPull().getResourceName();
    String objectToPullJson = readResourceFile(resourceName + ".json");
    JsonObject response = PARSER.parse(objectToPullJson).getAsJsonObject();
    Map<Integer, JsonObject> expected = new HashMap<>();
    response.getAsJsonArray(OracleConstants.ITEMS).forEach(jsonElement -> {
      JsonObject objectToPull = jsonElement.getAsJsonObject();
      expected.put(objectToPull.get("id").getAsInt(), objectToPull);
    });
    return expected;
  }

  @Test
  public void testSource() throws Exception {
    Map<String, String> properties = sourceProperties(getObjectToPull().getDisplayName());
    List<StructuredRecord> records = getPipelineResults(properties);
    Assert.assertEquals(3, records.size());
    for (StructuredRecord actual : records) {
      Integer id = actual.<Integer>get("id");
      Assert.assertNotNull(id);
      assertEquals(expected.get(id), actual);
    }
  }

  @Test
  public void testSourceWithSchemaSet() throws Exception {
    String objectToPull = getObjectToPull().getDisplayName();
    Schema schema = getObjectToPull().getSchema();
    Map<String, String> properties = sourceProperties(objectToPull, schema);
    List<StructuredRecord> records = getPipelineResults(properties);
    Assert.assertEquals(3, records.size());
    for (StructuredRecord actual : records) {
      Integer id = actual.<Integer>get("id");
      Assert.assertNotNull(id);
      assertEquals(expected.get(id), actual);
    }
  }

  public abstract OracleObject getObjectToPull();

  public abstract void assertEquals(JsonObject expected, StructuredRecord actual);

  protected void assertLinksEqual(JsonObject expectedLinked, StructuredRecord actualLinked) {
    JsonArray expectedLinks = expectedLinked.get("links").getAsJsonArray();
    List<StructuredRecord> actualLinks = actualLinked.get("links");
    for (int i = 0; i < expectedLinks.size(); i++) {
      JsonObject link = expectedLinks.get(i).getAsJsonObject();
      StructuredRecord actualLink = actualLinks.get(i);
      Assert.assertEquals(link.get("rel").getAsString(), actualLink.get("rel"));
      Assert.assertEquals(link.get("href").getAsString(), actualLink.get("href"));
    }
  }

  protected void assertRefsEqual(JsonObject expectedReference, StructuredRecord actualReference) {
    Assert.assertEquals(expectedReference.get("id").getAsInt(), (int) actualReference.get("id"));
    Assert.assertEquals(expectedReference.get("lookupName").getAsString(), actualReference.get("lookupName"));
  }

  protected Map<String, String> sourceProperties(String objectToPull) {
    return sourceProperties(objectToPull, null);
  }

  protected Map<String, String> sourceProperties(String objectToPull, @Nullable Schema schema) {
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
