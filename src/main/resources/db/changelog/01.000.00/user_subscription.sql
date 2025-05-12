--liquibase formatted sql
--changeset Siarhei_Kavaleu:db localFilePath:01.000.00/user_subscription.sql
CREATE TABLE user_subscription (
user_id             UUID        NOT NULL,
subscription_id     UUID        NOT NULL,
PRIMARY KEY (user_id, subscription_id),
CONSTRAINT fk_user_subscription_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
CONSTRAINT fk_user_subscription_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions(subscription_id) ON DELETE CASCADE
);