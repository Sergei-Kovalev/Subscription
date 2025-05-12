--liquibase formatted sql
--changeset Siarhei_Kavaleu:db localFilePath:01.000.00/subscriptions.sql
CREATE TABLE subscriptions
(
    subscription_id         UUID                            NOT NULL,
    digital_service_name    VARCHAR(100)                    NOT NULL,
    CONSTRAINT pk_subscriptions PRIMARY KEY (subscription_id)
);