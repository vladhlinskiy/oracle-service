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

public class AccountsFullObjectSourceETLTest extends OracleFullObjectSourceETLTestBase {

  @Override
  public OracleObject getObjectToPull() {
    return OracleObject.ACCOUNT;
  }

  @Override
  public void assertEquals(JsonObject expected, StructuredRecord actual) {
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
}
