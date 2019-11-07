# Oracle Service Cloud Batch Source

Description
-----------
Reads objects from Oracle Service Cloud.
Examples of objects are [Accounts], [Analytics Reports], [Opportunities], etc.

The data which should be read is specified using ROQL queries ([RightNow Object Query Language] queries)
or using Oracle Service Cloud object type and range date filters.

[RightNow Object Query Language]:
https://docs.oracle.com/cloud/latest/soa121300/TKRDP/GUID-0D1B2623-1C65-4966-B715-12F58E0CDE9B.htm#TKRDP2710

Configuration
-------------

**Reference Name:** Name used to uniquely identify this source for lineage, annotating metadata, etc.

**REST Server URL:** REST server URL from the account creation email that you receive from Oracle.
If you do not have this information, contact your Oracle Cloud Account administrator.

**Authentication Type:** Specifies the mode of authentication to use.

**Username:** Oracle Service Cloud username. Only used for Basic authentication.

**Password:** Oracle Service Cloud password. Only used for Basic authentication.

**Session:** Session ID for Oracle Service Cloud. Only used for Session authentication.

**Access Token:** OAuth access token, generated by an external identity provider.

**Query Type:** Specifies the type of query.

**Object To Pull:** Select an object to pull from Oracle Service Cloud. Examples of objects are [Accounts],
[Analytics Reports], [Opportunities], etc.

**ROQL Query:** ROQL query that specifies the data to import. Only required when Query Type is ROQL.

**Output Schema:** Specifies the schema of the documents.

**Sort By:** Field in the selected object to sort the results by.

**Sort Direction:** Determines the sort direction: Ascending or Descending.

**Start Date:** Filter data to include only records which have modification date greater than or equal to the
specified date. The date must be provided in the date format:

|              Format              |       Format Syntax       |          Example          |
| -------------------------------- | ------------------------- | ------------------------- |
| Date, time, and time zone offset | YYYY-MM-DDThh:mm:ss+hh:mm | 1999-01-01T23:01:01+01:00 |
|                                  | YYYY-MM-DDThh:mm:ss-hh:mm | 1999-01-01T23:01:01-08:00 |
|                                  | YYYY-MM-DDThh:mm:ssZ      | 1999-01-01T23:01:01Z      |

**End Date:** Filter data to include only records which have modification date less than the specified date.
The date must be provided in the date format:

|              Format              |       Format Syntax       |          Example          |
| -------------------------------- | ------------------------- | ------------------------- |
| Date, time, and time zone offset | YYYY-MM-DDThh:mm:ss+hh:mm | 1999-01-01T23:01:01+01:00 |
|                                  | YYYY-MM-DDThh:mm:ss-hh:mm | 1999-01-01T23:01:01-08:00 |
|                                  | YYYY-MM-DDThh:mm:ssZ      | 1999-01-01T23:01:01Z      |

Specifying this along with **Start Date** allows reading data modified within a specific time window. 
If no value is provided, no upper bound is applied.

[Accounts]:
https://docs.oracle.com/en/cloud/saas/service/19c/cxsvc/api-accounts.html

[Analytics Reports]:
https://docs.oracle.com/en/cloud/saas/service/19c/cxsvc/api-analytics-reports.html

[Opportunities]:
https://docs.oracle.com/en/cloud/saas/service/19c/cxsvc/api-opportunities.html

Data Types Mapping
----------

    | JSON Data Type                  | CDAP Schema Data Type              | Comment
    | ------------------------------- | ---------------------------------- | --------------------------- |
    | Boolean                         | boolean                            |                             |
    | Number                          | string, int, long, double, decimal | Mapped to string by default |
    | String                          | string                             |                             |
    | Object                          | record, map                        |                             |
    | Array                           | array                              |                             |
    