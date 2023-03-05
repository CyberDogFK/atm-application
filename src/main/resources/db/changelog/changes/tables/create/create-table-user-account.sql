--liquibase formatted sql
--changeset <antonpvliuk>:<create-table-user-accounts>
CREATE TABLE `users_accounts` (
                              `user_id` BIGINT NOT NULL,
                              `accounts_id` BIGINT NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(id),
                              FOREIGN KEY (accounts_id) REFERENCES account(id)
);
