--liquibase formatted sql
--changeset Siarhei_Kavaleu:db localFilePath:01.000.00/users.sql
CREATE TABLE users
(
    user_id             UUID                            NOT NULL,
    first_name          VARCHAR(30)                     NOT NULL,
    last_name           VARCHAR(30)                     NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);