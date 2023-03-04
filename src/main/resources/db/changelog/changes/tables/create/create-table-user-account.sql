--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-user-accounts>
CREATE TABLE `user_accounts` (
                              `user_id` BIGINT NOT NULL,
                              `account_id` BIGINT NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(id),
                              FOREIGN KEY (account_id) REFERENCES account(id)
);
