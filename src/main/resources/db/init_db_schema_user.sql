CREATE USER username WITH ENCRYPTED PASSWORD 'password';

DROP DATABASE IF EXISTS db;

CREATE DATABASE db WITH
    OWNER = username
    ENCODING = 'UTF8'
    LC_COLLATE = 'Polish_Poland.1252'
    LC_CTYPE = 'Polish_Poland.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = false;

ALTER DATABASE db SET lc_messages TO 'C';

-- \connect db
DROP SCHEMA IF EXISTS data;
CREATE SCHEMA data AUTHORIZATION username;
-- GRANT ALL ON SCHEMA data TO username;
-- ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA data GRANT ALL ON TABLES TO username;