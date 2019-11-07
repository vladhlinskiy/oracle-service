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

import io.cdap.cdap.etl.api.validation.CauseAttributes;
import io.cdap.cdap.etl.api.validation.ValidationFailure;
import io.cdap.cdap.etl.mock.validation.MockFailureCollector;
import io.cdap.plugin.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/**
 * Tests of {@link OracleConfig} methods.
 */
public class OracleConfigTest {

  private static final String MOCK_STAGE = "mockstage";

  private OracleConfigBuilder getValidConfigBuilder() {
    return OracleConfigBuilder.builder()
      .setReferenceName("OracleSource")
      .setAuthenticationTypeName(AuthenticationType.BASIC.getDisplayName())
      .setUser("user")
      .setPassword("password")
      .setQueryTypeName(QueryType.ROQL.getDisplayName())
      .setQuery("Select Contact from Contact where Contact.Name.First = 'Lin' AND Contact.Address.City = 'CA'");
  }

  @Test
  public void testValidateValid() {
    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    getValidConfigBuilder().build().validate(failureCollector);
    Assert.assertTrue(failureCollector.getValidationFailures().isEmpty());
  }

  @Test
  public void testValidateReferenceNameNull() {
    OracleConfig config = getValidConfigBuilder()
      .setReferenceName(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, Constants.Reference.REFERENCE_NAME);
  }

  @Test
  public void testValidateReferenceNameEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setReferenceName("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, Constants.Reference.REFERENCE_NAME);
  }

  @Test
  public void testValidateReferenceNameInvalid() {
    OracleConfig config = getValidConfigBuilder()
      .setReferenceName("**********")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, Constants.Reference.REFERENCE_NAME);
  }

  @Test
  public void testValidateAuthenticationTypeNull() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.AUTHENTICATION_TYPE);
  }

  @Test
  public void testValidateAuthenticationTypeEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.AUTHENTICATION_TYPE);
  }

  @Test
  public void testValidateAuthenticationTypeInvalid() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName("invalid-auth-type")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.AUTHENTICATION_TYPE);
  }

  @Test
  public void testValidateQueryTypeNull() {
    OracleConfig config = getValidConfigBuilder()
      .setQueryTypeName(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.QUERY_TYPE);
  }

  @Test
  public void testValidateQueryTypeEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setQueryTypeName("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.QUERY_TYPE);
  }

  @Test
  public void testValidateQueryTypeInvalid() {
    OracleConfig config = getValidConfigBuilder()
      .setQueryTypeName("invalid-query-type")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.QUERY_TYPE);
  }

  @Test
  public void testValidateQueryNull() {
    OracleConfig config = getValidConfigBuilder()
      .setQueryTypeName(QueryType.ROQL.getDisplayName())
      .setQuery(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.QUERY);
  }

  @Test
  public void testValidateQueryEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setQueryTypeName(QueryType.ROQL.getDisplayName())
      .setQuery("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.QUERY);
  }

  @Test
  public void testValidateObjectNameNull() {
    OracleConfig config = getValidConfigBuilder()
      .setQueryTypeName(QueryType.FULL_OBJECT.getDisplayName())
      .setServiceCloudObject(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.SERVICE_CLOUD_OBJECT);
  }

  @Test
  public void testValidateObjectNameEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setQueryTypeName(QueryType.FULL_OBJECT.getDisplayName())
      .setServiceCloudObject("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.SERVICE_CLOUD_OBJECT);
  }

  @Test
  public void testValidateUsernameNull() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(AuthenticationType.BASIC.getDisplayName())
      .setUser(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.USERNAME);
  }

  @Test
  public void testValidateUsernameEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(AuthenticationType.BASIC.getDisplayName())
      .setUser("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.USERNAME);
  }

  @Test
  public void testValidatePasswordNull() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(AuthenticationType.BASIC.getDisplayName())
      .setPassword(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.PASSWORD);
  }

  @Test
  public void testValidatePasswordEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(AuthenticationType.BASIC.getDisplayName())
      .setPassword("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.PASSWORD);
  }

  @Test
  public void testValidateSessionNull() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(AuthenticationType.SESSION.getDisplayName())
      .setSessionId(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.SESSION_ID);
  }

  @Test
  public void testValidateSessionEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(AuthenticationType.SESSION.getDisplayName())
      .setSessionId("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.SESSION_ID);
  }

  @Test
  public void testValidateAccessTokenNull() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(AuthenticationType.OAUTH.getDisplayName())
      .setAccessToken(null)
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.ACCESS_TOKEN);
  }

  @Test
  public void testValidateAccessTokenEmpty() {
    OracleConfig config = getValidConfigBuilder()
      .setAuthenticationTypeName(AuthenticationType.OAUTH.getDisplayName())
      .setAccessToken("")
      .build();

    MockFailureCollector failureCollector = new MockFailureCollector(MOCK_STAGE);
    config.validate(failureCollector);
    assertValidationFailed(failureCollector, OracleConstants.ACCESS_TOKEN);
  }

  protected static void assertValidationFailed(MockFailureCollector failureCollector, String paramName) {
    List<ValidationFailure> failureList = failureCollector.getValidationFailures();
    Assert.assertEquals(1, failureList.size());
    ValidationFailure failure = failureList.get(0);
    List<ValidationFailure.Cause> causeList = getCauses(failure, CauseAttributes.STAGE_CONFIG);
    Assert.assertEquals(1, causeList.size());
    ValidationFailure.Cause cause = causeList.get(0);
    Assert.assertEquals(paramName, cause.getAttribute(CauseAttributes.STAGE_CONFIG));
  }

  @Nonnull
  private static List<ValidationFailure.Cause> getCauses(ValidationFailure failure, String attribute) {
    return failure.getCauses()
      .stream()
      .filter(cause -> cause.getAttribute(attribute) != null)
      .collect(Collectors.toList());
  }
}
