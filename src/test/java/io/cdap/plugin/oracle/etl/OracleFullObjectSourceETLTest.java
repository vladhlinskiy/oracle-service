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
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.oracle.AuthenticationType;
import io.cdap.plugin.oracle.OracleConstants;
import io.cdap.plugin.oracle.OracleObject;
import io.cdap.plugin.oracle.QueryType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class OracleFullObjectSourceETLTest extends BaseOracleSourceETLTest {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  @Before
  public void testSetup() throws Exception {
    for (OracleObject object : OracleObject.values()) {
      String resourceName = object.getResourceName();
      wireMockRule.stubFor(WireMock.get(WireMock.urlEqualTo(OracleConstants.API_ROOT_PATH + resourceName))
                             .willReturn(WireMock.aResponse().withBody(readResourceFile(resourceName + ".json"))));
    }
  }

  @Test
  public void testSourceAccounts() throws Exception {
    Schema schema = Schema.recordOf("schema",
                                    Schema.Field.of("id", Schema.of(Schema.Type.INT)),
                                    Schema.Field.of("lookupName", Schema.of(Schema.Type.STRING)));
    Map<String, String> properties = sourceProperties(OracleObject.ACCOUNT, schema);
    List<StructuredRecord> records = getPipelineResults(properties);
    Assert.assertEquals(3, records.size());
  }

  private Map<String, String> sourceProperties(OracleObject objectToPull, Schema schema) {
    return new ImmutableMap.Builder<String, String>()
      .put(OracleConstants.SERVER_URL, getServerAddress())
      .put(OracleConstants.AUTHENTICATION_TYPE, AuthenticationType.BASIC.getDisplayName())
      .put(OracleConstants.USERNAME, USERNAME)
      .put(OracleConstants.PASSWORD, PASSWORD)
      .put(OracleConstants.QUERY_TYPE, QueryType.FULL_OBJECT.getDisplayName())
      .put(OracleConstants.SERVICE_CLOUD_OBJECT, objectToPull.getDisplayName())
      .put(OracleConstants.SCHEMA, schema.toString())
      .build();
  }
}
