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
 * The Analytics Report definition provides descriptions of a report's output columns, search criteria, and other
 * components. Oracle Service Cloud Analytics Report is mapped to a record with
 * "{@value AnalyticsReportsSchema#COLUMNS}" for the columns in an analytics report,
 * "{@value AnalyticsReportsSchema#FILTERS}" for the filters used for running an analytics report,
 * "{@value AnalyticsReportsSchema#NAME}" for the name of the report in the language of the current interface,
 * "{@value AnalyticsReportsSchema#NAMES}" for the  language-specific strings used for localization of fields.
 * The labels are assembled in a list associated with a particular text field.
 * <p>
 * See:
 * <a href=
 * "https://docs.oracle.com/en/cloud/saas/service/19c/cxsvc/op-services-rest-connect-v1.4-analyticsreports-id-get.html"
 * >Get an analytics report</a>
 */
public class AnalyticsReportsSchema extends OracleObjectSchema {

  private AnalyticsReportsSchema() {
    throw new AssertionError("Should not instantiate static utility class.");
  }

  public static final String COLUMNS = "columns";
  public static final String FILTERS = "filters";
  public static final String NAME = "name";
  public static final String NAMES = "names";

  public static final Schema SCHEMA = Schema.recordOf(
    "analytics-report-record",
    Schema.Field.of(ID, Schema.nullableOf(Schema.of(Schema.Type.INT))),
    Schema.Field.of(CREATED_TIME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(UPDATED_TIME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(LOOKUP_NAME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(COLUMNS, Schema.nullableOf(Columns.SCHEMA)),
    Schema.Field.of(FILTERS, Schema.nullableOf(Filters.SCHEMA)),
    Schema.Field.of(NAME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
    Schema.Field.of(NAMES, Schema.nullableOf(Names.SCHEMA))
  );

  /**
   * Oracle Service Cloud Analytics Reports Columns mapped to a record with
   * "{@value AnalyticsReportsSchema.Columns#DATA_TYPE}" for an ID which has an associated name string(these IDs can be
   * set by either value or name),
   * "{@value AnalyticsReportsSchema.Columns#DESCRIPTION}" for the description of the column in the report,
   * "{@value AnalyticsReportsSchema.Columns#HEADING}" for the column heading in the report.
   */
  public static class Columns extends LinkedObject {
    public static final String DATA_TYPE = "dataType";
    public static final String DESCRIPTION = "description";
    public static final String HEADING = "heading";

    public static final Schema SCHEMA = Schema.recordOf(
      "analytics-reports-columns-record",
      Schema.Field.of(DATA_TYPE, Schema.nullableOf(IdLookupName.schema("reports-columns-data-type"))),
      Schema.Field.of(DESCRIPTION, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
      Schema.Field.of(HEADING, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
      Schema.Field.of(LINKS, Schema.arrayOf(Link.schema("report-columns")))
    );
  }

  /**
   * Oracle Service Cloud Analytics Reports Filters mapped to a record with
   * "{@value AnalyticsReportsSchema.Filters#ATTRIBUTES}" for the attributes associated with an analytics report filter,
   * "{@value AnalyticsReportsSchema.Filters#DATA_TYPE}" for an ID which has an associated name string(these IDs can be
   * set by either value or name),
   * "{@value AnalyticsReportsSchema.Filters#NAME}" for the name of the filter,
   * "{@value AnalyticsReportsSchema.Filters#OPERATOR}" for an ID which has an associated name string(these IDs can be
   * set by either value or name),
   * "{@value AnalyticsReportsSchema.Filters#PROMPT}" for the prompt for the filter,
   * "{@value AnalyticsReportsSchema.Filters#VALUES}" for the string representation of the values for the operator to
   * apply. The right-hand side (RHS) of the filter value.
   */
  public static class Filters extends LinkedObject {
    public static final String ATTRIBUTES = "attributes";
    public static final String DATA_TYPE = "dataType";
    public static final String NAME = "name";
    public static final String OPERATOR = "operator";
    public static final String PROMPT = "prompt";
    public static final String VALUES = "values";

    public static final Schema SCHEMA = Schema.recordOf(
      "analytics-reports-filters-record",
      Schema.Field.of(ATTRIBUTES, Schema.nullableOf(FiltersAttributes.SCHEMA)),
      Schema.Field.of(DATA_TYPE, Schema.nullableOf(IdLookupName.schema("reports-filters-data-type"))),
      Schema.Field.of(NAME, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
      Schema.Field.of(OPERATOR, Schema.nullableOf(IdLookupName.schema("reports-filters-operator"))),
      Schema.Field.of(PROMPT, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
      Schema.Field.of(VALUES, Schema.nullableOf(Schema.arrayOf(Schema.of(Schema.Type.STRING)))),
      Schema.Field.of(LINKS, Schema.arrayOf(Link.schema("report-filters")))
    );
  }

  /**
   * Oracle Service Cloud Analytics Reports Columns mapped to a record with
   * "{@value AnalyticsReportsSchema.FiltersAttributes#EDITABLE}" for the attribute which indicates whether the filter
   * is editable,
   * "{@value AnalyticsReportsSchema.FiltersAttributes#REQUIRED}" for the attribute which indicates whether the filter
   * is mandatory.
   */
  public static class FiltersAttributes {
    public static final String EDITABLE = "editable";
    public static final String REQUIRED = "required";

    public static final Schema SCHEMA = Schema.recordOf(
      "analytics-reports-filters-attributes-record",
      Schema.Field.of(EDITABLE, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(REQUIRED, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN)))
    );
  }

  /**
   * Oracle Service Cloud Analytics Reports Columns mapped to a record with
   * "{@value AnalyticsReportsSchema.Names#LABEL_TEXT}" for the language-specific label text,
   * "{@value AnalyticsReportsSchema.Names#LANGUAGE}" for an ID which has an associated name string(these IDs can be
   * set by either value or name),
   */
  public static class Names extends LinkedObject {
    public static final String LABEL_TEXT = "labelText";
    public static final String LANGUAGE = "language";

    public static final Schema SCHEMA = Schema.recordOf(
      "analytics-reports-names-record",
      Schema.Field.of(LABEL_TEXT, Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))),
      Schema.Field.of(LANGUAGE, Schema.nullableOf(IdLookupName.schema("reports-names-language"))),
      Schema.Field.of(LINKS, Schema.arrayOf(Link.schema("analytics-reports-names-filters")))
    );
  }
}
