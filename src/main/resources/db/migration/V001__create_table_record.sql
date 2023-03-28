CREATE TABLE DATA.RECORD
(
    RECORD_ID         VARCHAR(36) DEFAULT random_uuid() NOT NULL PRIMARY KEY,
    RECORD_FIRST_NAME VARCHAR(255)                      NOT NULL,
    RECORD_PHONE_NO   VARCHAR(9)                        NOT NULL,
    RECORD_AMOUNT     NUMERIC(10, 2)                    NOT NULL
);