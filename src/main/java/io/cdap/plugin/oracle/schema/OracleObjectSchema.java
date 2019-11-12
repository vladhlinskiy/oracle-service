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
 * Oracle Service Cloud Object is mapped to a record with
 * "{@value OracleObjectSchema#ID}" for the unique identifier of the object,
 * "{@value OracleObjectSchema#CREATED_TIME}" for the date and time when the asset was created,
 * "{@value OracleObjectSchema#UPDATED_TIME}" for the date and time when the asset was last updated,
 * "{@value OracleObjectSchema#LOOKUP_NAME}" for the name used to look up the object.
 */
public abstract class OracleObjectSchema {

  public static final String ID = "id";
  public static final String CREATED_TIME = "createdTime";
  public static final String UPDATED_TIME = "updatedTime";
  public static final String LOOKUP_NAME = "lookupName";

  /**
   * Oracle Service Cloud Object is mapped to a record with
   * "{@value OracleObjectSchema.LinkedObject#LINKS}" for an array of {@link Link}.
   */
  public static class LinkedObject {
    public static final String LINKS = "links";

    public static Schema schema(String fieldName) {
      return Schema.recordOf(
        fieldName + "-linked-object-record",
        Schema.Field.of(LINKS, Schema.arrayOf(Link.schema(fieldName)))
      );
    }
  }

  /**
   * Oracle Service Cloud Link is mapped to a record with
   * "{@value OracleObjectSchema.Link#REL}" for the relationship between two linked documents,
   * "{@value OracleObjectSchema.Link#HREF}" for the reference to the linked document,
   * "{@value OracleObjectSchema.Link#MEDIA_TYPE}" for the media type.
   */
  public static class Link {
    public static final String REL = "rel";
    public static final String HREF = "href";
    public static final String MEDIA_TYPE = "mediaType";

    public static Schema schema(String fieldName) {
      return Schema.recordOf(
        fieldName + "-link-record",
        Schema.Field.of(REL, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
        Schema.Field.of(HREF, Schema.nullableOf(Schema.of(Schema.Type.STRING))),
        Schema.Field.of(MEDIA_TYPE, Schema.nullableOf(Schema.of(Schema.Type.STRING)))
      );
    }
  }

  /**
   * An Oracle Service Cloud ID which has an associated name string. These IDs can be set by either value or name.
   * "{@value OracleObjectSchema.IdLookupName#ID}" for the resource's identifier,
   * "{@value OracleObjectSchema.IdLookupName#LOOKUP_NAME}" for the resource's lookup name.
   */
  public static class IdLookupName {
    public static final String ID = "id";
    public static final String LOOKUP_NAME = "lookupName";

    public static Schema schema(String fieldName) {
      return Schema.recordOf(
        fieldName + "-id-lookup-name-record",
        Schema.Field.of(ID, Schema.nullableOf(Schema.of(Schema.Type.INT))),
        Schema.Field.of(LOOKUP_NAME, Schema.nullableOf(Schema.of(Schema.Type.STRING)))
      );
    }
  }
}
