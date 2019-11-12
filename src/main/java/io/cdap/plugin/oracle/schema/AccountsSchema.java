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

package io.cdap.plugin.oracle.schema;

import io.cdap.cdap.api.data.schema.Schema;

/**
 * The Account represents a staff member in Oracle Service Cloud. Examples of staff members are customer sales
 * representatives, sales agents, site administrators, and so on.
 * Oracle Service Cloud Account is mapped to a record with
 * "{@value AccountsSchema#ACCOUNT_HIERARCHY}" for the reference to a resource in 'accounts' collection,
 * "{@value AccountsSchema#ATTRIBUTES}" for the group of flags used for setting certain features or special
 * behaviors for an account,
 * "{@value AccountsSchema#COUNTRY}" for the the details about the countries and provinces which are used to
 * maintain accurate address information for the organizations and contacts in Oracle Service Cloud(reference to a
 * resource 'countries' collection),
 * "{@value AccountsSchema#DISPLAY_NAME}" for the display name of the account,
 * "{@value AccountsSchema#DISPLAY_ORDER}" for the order in which this staff account is displayed relative
 * to other members of the same group,
 * "{@value AccountsSchema#EMAIL_NOTIFICATION}" for an ID which has an associated name string(these IDs can
 * be set by either value or name),
 * "{@value AccountsSchema#EMAILS}" for the email address and its associated information,
 * "{@value AccountsSchema#LOGIN}" for the user name used for authentication,
 * "{@value AccountsSchema#MANAGER}" for the account that represents a staff member in Oracle Service Cloud.
 * Examples of staff members are customer sales representatives, sales agents, site administrators, and so on.
 * It is the reference to a resource in 'accounts' collection. Only ID or lookupName can be provided to specify the
 * resource.
 * "{@value AccountsSchema#NAME}" for the full name of the person, including the first name and the last
 * name,
 * "{@value AccountsSchema#NAME_FURIGANA}" for the full name of the person, including the first name and the
 * last name,
 * "{@value AccountsSchema#NOTIFICATION_PENDING}" for the attribute which indicates whether any notifications
 * are pending,
 * "{@value AccountsSchema#PASSWORD_EXPIRATION_TIME}" for the date and time when the password is set to
 * expire,
 * "{@value AccountsSchema#PHONES}" for the details related to the phone number,
 * "{@value AccountsSchema#PROFILE}" for an ID which has an associated name string(these IDs can be set by
 * either value or name),
 * "{@value AccountsSchema#SALES_SETTINGS}" for the sales-related information of the account,
 * "{@value AccountsSchema#SERVICE_SETTINGS}" for the service-related information of the account,
 * "{@value AccountsSchema#SIGNATURE}" for the email signature of the account,
 * "{@value AccountsSchema#STAFF_GROUP}" for an ID which has an associated name string(these IDs can be set
 * by either value or name).
 * <p>
 * See:
 * <a
 * href="https://docs.oracle.com/en/cloud/saas/service/19c/cxsvc/op-services-rest-connect-v1.4-accounts-id-get.html">
 * Get an account
 * </a>
 */
public class AccountsSchema extends OracleObjectSchema {

  private AccountsSchema() {
    throw new AssertionError("Should not instantiate static utility class.");
  }

  public static final String ACCOUNT_HIERARCHY = "accountHierarchy";
  public static final String ATTRIBUTES = "attributes";
  public static final String COUNTRY = "country";
  public static final String DISPLAY_NAME = "displayName";
  public static final String DISPLAY_ORDER = "displayOrder";
  public static final String EMAIL_NOTIFICATION = "emailNotification";
  public static final String EMAILS = "emails";
  public static final String LOGIN = "login";
  public static final String MANAGER = "manager";
  public static final String NAME = "name";
  public static final String NAME_FURIGANA = "nameFurigana";
  public static final String NOTIFICATION_PENDING = "notificationPending";
  public static final String PASSWORD_EXPIRATION_TIME = "passwordExpirationTime";
  public static final String PHONES = "phones";
  public static final String PROFILE = "profile";
  public static final String SALES_SETTINGS = "salesSettings";
  public static final String SERVICE_SETTINGS = "serviceSettings";
  public static final String SIGNATURE = "signature";
  public static final String STAFF_GROUP = "staffGroup";

  public static final Schema SCHEMA = Schema.recordOf(
    "account-record",
    Schema.Field.of(ID, Schema.nullableOf(Schema.of(Schema.Type.INT))),
    Schema.Field.of(CREATED_TIME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(UPDATED_TIME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(LOOKUP_NAME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(ACCOUNT_HIERARCHY, Schema.nullableOf(LinkedObject.schema("account-hierarchy"))),
    Schema.Field.of(ATTRIBUTES, AccountAttributes.SCHEMA),
    Schema.Field.of(COUNTRY, Schema.nullableOf(LinkedObject.schema("country"))),
    Schema.Field.of(DISPLAY_NAME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(DISPLAY_ORDER, Schema.nullableOf(Schema.of(Schema.Type.INT))),
    Schema.Field.of(EMAIL_NOTIFICATION, Schema.nullableOf(IdLookupName.schema("email-notification"))),
    Schema.Field.of(EMAILS, Schema.nullableOf(AccountEmails.SCHEMA)),
    Schema.Field.of(LOGIN, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(MANAGER, Schema.nullableOf(IdLookupName.schema("manager"))),
    Schema.Field.of(NAME, Schema.nullableOf(AccountName.schema("name"))),
    Schema.Field.of(NAME_FURIGANA, Schema.nullableOf(AccountName.schema("name-furigana"))),
    Schema.Field.of(NOTIFICATION_PENDING, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
    Schema.Field.of(PASSWORD_EXPIRATION_TIME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(PHONES, Schema.nullableOf(AccountPhone.SCHEMA)),
    Schema.Field.of(PROFILE, Schema.nullableOf(IdLookupName.schema("profile"))),
    Schema.Field.of(SALES_SETTINGS, Schema.nullableOf(AccountSalesSettings.SCHEMA)),
    Schema.Field.of(SERVICE_SETTINGS, Schema.nullableOf(AccountServiceSettings.SCHEMA)),
    Schema.Field.of(SIGNATURE, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(STAFF_GROUP, Schema.nullableOf(IdLookupName.schema("staff-group")))
  );

  /**
   * Oracle Service Cloud Account Attributes mapped to a record with
   * "{@value AccountsSchema.AccountAttributes#ACCOUNT_LOCKED}" for the attribute which indicates whether the account
   * is temporarily locked,
   * "{@value AccountsSchema.AccountAttributes#CAN_MODIFY_EMAIL_SIGNATURE}" for the attribute which indicates whether
   * the staff member can modify the email signature,
   * "{@value AccountsSchema.AccountAttributes#FORCE_PASSWORD_CHANGE}" for the attribute which indicates whether
   * the staff member must change the password at the next login,
   * "{@value AccountsSchema.AccountAttributes#INFREQUENT_USER}" for the attribute which indicates whether the staff
   * member is considered as an infrequent user seat, as opposed to a full seat,
   * "{@value AccountsSchema.AccountAttributes#PASSWORD_NEVER_EXPIRES}" for the attribute which indicates whether the
   * staff member's account password never expires,
   * "{@value AccountsSchema.AccountAttributes#PERMANENTLY_DISABLED}" for the attribute which indicates whether the
   * staff member's account is permanently disabled,
   * "{@value AccountsSchema.AccountAttributes#STAFF_ASSIGNMENT_DISABLED}" for the attribute which indicates whether
   * the staff member's account cannot be assigned to incidents, answers, opportunities, and tasks,
   * "{@value AccountsSchema.AccountAttributes#VIEWS_REPORTS_DISABLED}" for the attribute which indicates whether
   * the staff member's account cannot be included in the list for filters in reports,
   * "{@value AccountsSchema.AccountAttributes#VIRTUAL_ACCOUNT}" for the attribute which indicates whether the
   * staff member's account is virtual.
   */
  public static class AccountAttributes {
    public static final String ACCOUNT_LOCKED = "accountLocked";
    public static final String CAN_MODIFY_EMAIL_SIGNATURE = "canModifyEmailSignature";
    public static final String FORCE_PASSWORD_CHANGE = "forcePasswordChange";
    public static final String INFREQUENT_USER = "infrequentUser";
    public static final String PASSWORD_NEVER_EXPIRES = "passwordNeverExpires";
    public static final String PERMANENTLY_DISABLED = "permanentlyDisabled";
    public static final String STAFF_ASSIGNMENT_DISABLED = "staffAssignmentDisabled";
    public static final String VIEWS_REPORTS_DISABLED = "viewsReportsDisabled";
    public static final String VIRTUAL_ACCOUNT = "virtualAccount";

    public static final Schema SCHEMA = Schema.recordOf(
      "account-attributes-record",
      Schema.Field.of(ACCOUNT_LOCKED, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(CAN_MODIFY_EMAIL_SIGNATURE, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(FORCE_PASSWORD_CHANGE, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(INFREQUENT_USER, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(PASSWORD_NEVER_EXPIRES, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(PERMANENTLY_DISABLED, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(STAFF_ASSIGNMENT_DISABLED, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(VIEWS_REPORTS_DISABLED, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(VIRTUAL_ACCOUNT, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN)))
    );
  }

  /**
   * Oracle Service Cloud Account emails mapped to a record with
   * "{@value AccountsSchema.AccountEmails#ADDRESS}" for the string value of the email address,
   * "{@value AccountsSchema.AccountEmails#ADDRESS_TYPE}"  an ID which has an associated name string(these IDs can
   * be set by either value or name),
   * "{@value AccountsSchema.AccountEmails#CERTIFICATE}" for the associated public email certificate used to encrypt
   * outgoing emails,
   * "{@value AccountsSchema.AccountEmails#INVALID}" for attribute which indicates whether the email address is
   * disabled.
   */
  public static class AccountEmails extends LinkedObject {
    public static final String ADDRESS = "address";
    public static final String ADDRESS_TYPE = "addressType";
    public static final String CERTIFICATE = "certificate";
    public static final String INVALID = "invalid";

    public static final Schema SCHEMA = Schema.recordOf(
      "account-emails-record",
      Schema.Field.of(ADDRESS, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
      Schema.Field.of(ADDRESS_TYPE, Schema.nullableOf(IdLookupName.schema("account-address-type-record"))),
      Schema.Field.of(CERTIFICATE, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
      Schema.Field.of(INVALID, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(LINKS, Schema.arrayOf(Link.schema("account-emails")))
    );
  }

  /**
   * Oracle Service Cloud Account name mapped to a record with
   * "{@value AccountsSchema.AccountName#FIRST}" for the first name of the person,
   * "{@value AccountsSchema.AccountName#LAST}"  for the last name of the person.
   */
  public static class AccountName {
    public static final String FIRST = "first";
    public static final String LAST = "last";

    public static Schema schema(String fieldName) {
      return Schema.recordOf(
        fieldName + "-account-name-record",
        Schema.Field.of(FIRST, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
        Schema.Field.of(LAST, Schema.nullableOf(Schema.of(Schema.Type.STRING)))
      );
    }
  }

  /**
   * Oracle Service Cloud Account phone mapped to a record with
   * "{@value AccountsSchema.AccountPhone#NUMBER}" for the free-form phone number including non-numeric characters.
   * For example, 1 (406) 522-4200,
   * "{@value AccountsSchema.AccountPhone#PHONE_TYPE}" for an ID which has an associated name string(these IDs can be
   * set by either value or name),
   * "{@value AccountsSchema.AccountPhone#RAW_NUMBER}" for the automatically populated numeric string derived by
   * excluding the non-numeric characters from the value of the Number attribute. For example, if the Number attribute
   * has a value of 1 (406) 522-4200, this field is populated as 14065224200.
   */
  public static class AccountPhone extends LinkedObject {
    public static final String NUMBER = "number";
    public static final String PHONE_TYPE = "phoneType";
    public static final String RAW_NUMBER = "rawNumber";

    public static final Schema SCHEMA = Schema.recordOf(
      "account-phone-record",
      Schema.Field.of(NUMBER, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
      Schema.Field.of(PHONE_TYPE, Schema.nullableOf(IdLookupName.schema("account-phone-type-record"))),
      Schema.Field.of(RAW_NUMBER, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
      Schema.Field.of(LINKS, Schema.arrayOf(Link.schema("account-phone")))
    );
  }

  /**
   * Oracle Service Cloud Account sales settings mapped to a record with
   * "{@value AccountsSchema.AccountSalesSettings#DEFAULT_CURRENCY}" for an ID which has an associated name string
   * (these IDs can be set by either value or name),
   * "{@value AccountsSchema.AccountSalesSettings#TERRITORY}" for the specific geographical sales region. Sales
   * representatives can be assigned specific territories for opportunity assignment. It is the reference to a
   * resource in 'salesTerritories' collection. Only ID or lookupName can be provided to specify the resource.
   */
  public static class AccountSalesSettings {
    public static final String DEFAULT_CURRENCY = "defaultCurrency";
    public static final String TERRITORY = "territory";

    public static final Schema SCHEMA = Schema.recordOf(
      "account-sales-settings-record",
      Schema.Field.of(DEFAULT_CURRENCY, Schema.nullableOf(IdLookupName.schema("default-currency"))),
      Schema.Field.of(TERRITORY, Schema.nullableOf(IdLookupName.schema("territory")))
    );
  }

  /**
   * Oracle Service Cloud Account service settings mapped to a record with
   * "{@value AccountsSchema.AccountServiceSettings#SCREEN_POP_PORT}" for the port number assigned for screen-pop
   * alerts for the staff account. It is used in Citrix and Terminal Services environments where there are multiple
   * agents logged in to different sessions on the same machine.
   */
  public static class AccountServiceSettings {
    public static final String SCREEN_POP_PORT = "screenPopPort";

    public static final Schema SCHEMA = Schema.recordOf(
      "account-service-settings-record",
      Schema.Field.of(SCREEN_POP_PORT, Schema.nullableOf(Schema.of(Schema.Type.INT)))
    );
  }
}
