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

import com.google.gson.JsonObject;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.plugin.oracle.OracleObject;
import org.junit.Assert;

public class AnalyticsReportsFullObjectSourceETLTest extends OracleFullObjectSourceETLTestBase {

  @Override
  public OracleObject getObjectToPull() {
    return OracleObject.ANALYTICS_REPORT;
  }

  @Override
  public void assertEquals(JsonObject expected, StructuredRecord actual) {
    Assert.assertEquals(expected.get("lookupName").getAsString(), actual.<String>get("lookupName"));
    Assert.assertEquals(expected.get("createdTime").getAsString(), actual.<String>get("createdTime"));
    Assert.assertEquals(expected.get("updatedTime").getAsString(), actual.<String>get("updatedTime"));
    Assert.assertEquals(expected.get("name").getAsString(), actual.<String>get("name"));
    assertLinksEqual(expected.get("columns").getAsJsonObject(), actual.get("columns"));
    assertLinksEqual(expected.get("filters").getAsJsonObject(), actual.get("filters"));
    assertLinksEqual(expected.get("names").getAsJsonObject(), actual.get("names"));
  }
}
